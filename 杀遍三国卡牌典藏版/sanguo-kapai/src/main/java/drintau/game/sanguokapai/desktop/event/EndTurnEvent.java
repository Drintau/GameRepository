package drintau.game.sanguokapai.desktop.event;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.EquipmentCard;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.desktop.ActionItem;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import drintau.game.sanguokapai.util.DaemonScheduler;
import drintau.game.sanguokapai.util.ThreadSleepUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EndTurnEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        DesktopContext desktopContext = DesktopContext.getInstance();

        desktopContext.getBeginTurn().setDisable(true);
        desktopContext.getSelectCard().setDisable(true);
        desktopContext.getEndTurn().setDisable(true);

        // 移动单位
        DaemonScheduler.getInstance().submitOnceDelayTask(() -> {
            StackPane[][] cells = desktopContext.getCells();

            ArrayDeque<ActionItem> actionDeque = desktopContext.getActionDeque();
            ActionItem actionItem;
            while ((actionItem = actionDeque.pollFirst()) != null) {
                if (actionItem.isFinishFlag()) {
                    continue;
                }

                UnitCard unitCard = actionItem.getUnitCard();

                //前进
                for (int i = unitCard.getSpeed(); i > 0 ; i--) {
                    // 当前行
                    int curRowIndex = actionItem.getCurRowIndex();
                    // 当前列，移动后就是前一列了
                    int preColIndex = actionItem.getCurColIndex();
                    int nextColIndex;
                    if (actionItem.isAiPlayer()) {
                        nextColIndex = actionItem.getCurColIndex() - 1;
                    } else {
                        nextColIndex = actionItem.getCurColIndex() + 1;
                    }

                    // 到达终点
                    if (actionItem.isAiPlayer() && desktopContext.getPeoplePlayer().beAttack(nextColIndex)) {
                        int eqAddAttack = calcEqAddAttack(actionItem);
                        actionItem.setAddAttack(eqAddAttack);
                        actionItem.setCurAttack(actionItem.getUnitCard().getBaseAttack() + actionItem.getAddAttack());
                        int lowerHP = actionItem.getCurAttack();
                        Platform.runLater(() -> {
                            desktopContext.getPeoplePlayer().getHp().set(desktopContext.getPeoplePlayer().getHp().get() - lowerHP);
                            cells[curRowIndex][preColIndex].getChildren().clear();
                            cells[curRowIndex][preColIndex].setUserData(null);
                            testGameOver();
                        });
                        actionItem.setFinishFlag(true);
                        ThreadSleepUtil.sleepSeconds(1L);
                        break;
                    } else if (!actionItem.isAiPlayer() && desktopContext.getAiPlayer().beAttack(nextColIndex)) {
                        int eqAddAttack = calcEqAddAttack(actionItem);
                        actionItem.setAddAttack(eqAddAttack);
                        actionItem.setCurAttack(actionItem.getUnitCard().getBaseAttack() + actionItem.getAddAttack());
                        int lowerHP = actionItem.getCurAttack();
                        Platform.runLater(() -> {
                            desktopContext.getAiPlayer().getHp().set(desktopContext.getAiPlayer().getHp().get() - lowerHP);
                            cells[curRowIndex][preColIndex].getChildren().clear();
                            cells[curRowIndex][preColIndex].setUserData(null);
                            testGameOver();
                        });
                        actionItem.setFinishFlag(true);
                        ThreadSleepUtil.sleepSeconds(1L);
                        break;
                    }

                    // 碰撞
                    if (!cells[curRowIndex][nextColIndex].getChildren().isEmpty()) {
                        ActionItem targetCellActionItem = (ActionItem) cells[curRowIndex][nextColIndex].getUserData();
                        // 敌方单位
                        if (actionItem.isAiPlayer() != targetCellActionItem.isAiPlayer()) {
                            actionItem.setAddAttack(0);
                            targetCellActionItem.setAddAttack(0);

                            // 属性克制
                            CardConstants.UnitType beatUnitType1 = desktopContext.ADVANTAGE_MAP.get(actionItem.getUnitCard().getUnitType());
                            CardConstants.UnitType beatUnitType2 = desktopContext.ADVANTAGE_MAP.get(targetCellActionItem.getUnitCard().getUnitType());

                            if (beatUnitType1 == targetCellActionItem.getUnitCard().getUnitType()) {
                                actionItem.setAddAttack(actionItem.getAddAttack() + 1);
                                targetCellActionItem.setAddAttack(targetCellActionItem.getAddAttack() - 1);
                            }
                            if (beatUnitType2 == actionItem.getUnitCard().getUnitType()) {
                                actionItem.setAddAttack(actionItem.getAddAttack() - 1);
                                targetCellActionItem.setAddAttack(targetCellActionItem.getAddAttack() + 1);
                            }

                            // 装备加成
                            int peopleEqAddAttack = 0;
                            if (!cells[curRowIndex][DesktopContext.getInstance().getPeoplePlayer().getEqColIndex()].getChildren().isEmpty()) {
                                // 玩家有装备
                                if (cells[curRowIndex][DesktopContext.getInstance().getPeoplePlayer().getEqColIndex()].getUserData() instanceof EquipmentCard equipmentCard) {
                                    if (equipmentCard.getUnitType() == CardConstants.UnitType.ALL || equipmentCard.getUnitType() == actionItem.getUnitCard().getUnitType()) {
                                        peopleEqAddAttack = equipmentCard.getAddAttack();
                                    }
                                }
                            }
                            int aiEqAddAttack = 0;
                            if (!cells[curRowIndex][DesktopContext.getInstance().getAiPlayer().getEqColIndex()].getChildren().isEmpty()) {
                                // 电脑有装备
                                if (cells[curRowIndex][DesktopContext.getInstance().getAiPlayer().getEqColIndex()].getUserData() instanceof EquipmentCard equipmentCard) {
                                    if (equipmentCard.getUnitType() == CardConstants.UnitType.ALL || equipmentCard.getUnitType() == targetCellActionItem.getUnitCard().getUnitType()) {
                                        aiEqAddAttack = equipmentCard.getAddAttack();
                                    }
                                }
                            }

                            if (!actionItem.isAiPlayer()) {
                                // 己方是人，对方是电脑
                                actionItem.setAddAttack(actionItem.getAddAttack() + peopleEqAddAttack);
                                targetCellActionItem.setAddAttack(targetCellActionItem.getAddAttack() + aiEqAddAttack);
                            } else {
                                // 己方是电脑，对方是人
                                actionItem.setAddAttack(actionItem.getAddAttack() + aiEqAddAttack);
                                targetCellActionItem.setAddAttack(targetCellActionItem.getAddAttack() + peopleEqAddAttack);
                            }

                            actionItem.setCurAttack(actionItem.getUnitCard().getBaseAttack() + actionItem.getAddAttack());
                            targetCellActionItem.setCurAttack(targetCellActionItem.getUnitCard().getBaseAttack() + targetCellActionItem.getAddAttack());

                            ActionItem finalActionItem = actionItem;
                            Platform.runLater(() -> {
                                Label peoplePlayerUnit;
                                Label aiPlayerUnit;
                                if (finalActionItem.isAiPlayer()) {
                                    aiPlayerUnit = new Label(finalActionItem.getAttackInfo());
                                    peoplePlayerUnit = new Label(targetCellActionItem.getAttackInfo());
                                } else {
                                    peoplePlayerUnit = new Label(finalActionItem.getAttackInfo());
                                    aiPlayerUnit = new Label(targetCellActionItem.getAttackInfo());
                                }
                                peoplePlayerUnit.setFont(StyleConstants.font16);
                                peoplePlayerUnit.setBackground(StyleConstants.PLAYER_UNIT_BACKGROUND);
                                aiPlayerUnit.setFont(StyleConstants.font16);
                                aiPlayerUnit.setBackground(StyleConstants.RED_BACKGROUND);
                                desktopContext.getAttackRoot().setLeft(peoplePlayerUnit);
                                desktopContext.getAttackRoot().setRight(aiPlayerUnit);
                                desktopContext.getRoot().getChildren().add(desktopContext.getAttackRoot());
                            });

                            synchronized (desktopContext.getBattleLock()) {
                                try {
                                    desktopContext.getBattleLock().wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }

                            ThreadSleepUtil.sleepSeconds(1L);

                            int u1Attack = actionItem.getCurAttack();
                            int u2Attack = targetCellActionItem.getCurAttack();
                            if (u1Attack > u2Attack) {
                                // u2死了
                                targetCellActionItem.setFinishFlag(true);
                                int removeColIndex = targetCellActionItem.getCurColIndex();
                                Platform.runLater(() -> {
                                    cells[curRowIndex][removeColIndex].getChildren().clear();
                                    cells[curRowIndex][removeColIndex].setUserData(null);
                                });
                                move(actionItem, nextColIndex);
                            } else if (u1Attack < u2Attack) {
                                actionItem.setFinishFlag(true);
                                int removeColIndex = actionItem.getCurColIndex();
                                Platform.runLater(() -> {
                                    cells[curRowIndex][removeColIndex].getChildren().clear();
                                    cells[curRowIndex][removeColIndex].setUserData(null);
                                });
                            } else {
                                actionItem.setFinishFlag(true);
                                targetCellActionItem.setFinishFlag(true);
                                int removeColIndex1 = actionItem.getCurColIndex();
                                int removeColIndex2 = targetCellActionItem.getCurColIndex();
                                Platform.runLater(() -> {
                                    cells[curRowIndex][removeColIndex1].getChildren().clear();
                                    cells[curRowIndex][removeColIndex1].setUserData(null);
                                    cells[curRowIndex][removeColIndex2].getChildren().clear();
                                    cells[curRowIndex][removeColIndex2].setUserData(null);
                                });
                            }
                            ThreadSleepUtil.sleepSeconds(1L);
                            break;
                        } else {
                            // 己方单位
                            // 当前单位剩余移动力大于等于2，且前面第二格无归属，就超越，移动力-1；如果前面第二格是地方阵营，那就是攻入
                            // 剩余移动力就是当前的i
                            if (i >= 2) {
                                int nextColIndex2;
                                if (actionItem.isAiPlayer()) {
                                    nextColIndex2 = nextColIndex - 1;
                                } else {
                                    nextColIndex2 = nextColIndex + 1;
                                }
                                // 到达终点
                                if (actionItem.isAiPlayer() && desktopContext.getPeoplePlayer().beAttack(nextColIndex2)) {
                                    int aiEqAddAttack = 0;
                                    if (!cells[curRowIndex][DesktopContext.getInstance().getAiPlayer().getEqColIndex()].getChildren().isEmpty()) {
                                        // 电脑有装备
                                        if (cells[curRowIndex][DesktopContext.getInstance().getAiPlayer().getEqColIndex()].getUserData() instanceof EquipmentCard equipmentCard) {
                                            if (equipmentCard.getUnitType() == CardConstants.UnitType.ALL || equipmentCard.getUnitType() == actionItem.getUnitCard().getUnitType()) {
                                                aiEqAddAttack = equipmentCard.getAddAttack();
                                            }
                                        }
                                    }
                                    actionItem.setAddAttack(aiEqAddAttack);
                                    actionItem.setCurAttack(actionItem.getUnitCard().getBaseAttack() + actionItem.getAddAttack());
                                    int lowerHP = actionItem.getCurAttack();
                                    Platform.runLater(() -> {
                                        desktopContext.getPeoplePlayer().getHp().set(desktopContext.getPeoplePlayer().getHp().get() - lowerHP);
                                        cells[curRowIndex][preColIndex].getChildren().clear();
                                        cells[curRowIndex][preColIndex].setUserData(null);
                                    });
                                    actionItem.setFinishFlag(true);
                                    ThreadSleepUtil.sleepSeconds(1L);
                                    break;
                                } else if (!actionItem.isAiPlayer() && desktopContext.getAiPlayer().beAttack(nextColIndex2)) {
                                    int peopleEqAddAttack = 0;
                                    if (!cells[curRowIndex][DesktopContext.getInstance().getPeoplePlayer().getEqColIndex()].getChildren().isEmpty()) {
                                        // 玩家有装备
                                        if (cells[curRowIndex][DesktopContext.getInstance().getPeoplePlayer().getEqColIndex()].getUserData() instanceof EquipmentCard equipmentCard) {
                                            if (equipmentCard.getUnitType() == CardConstants.UnitType.ALL || equipmentCard.getUnitType() == actionItem.getUnitCard().getUnitType()) {
                                                peopleEqAddAttack = equipmentCard.getAddAttack();
                                            }
                                        }
                                    }
                                    actionItem.setAddAttack(peopleEqAddAttack);
                                    actionItem.setCurAttack(actionItem.getUnitCard().getBaseAttack() + actionItem.getAddAttack());
                                    int lowerHP = actionItem.getCurAttack();
                                    Platform.runLater(() -> {
                                        desktopContext.getAiPlayer().getHp().set(desktopContext.getAiPlayer().getHp().get() - lowerHP);
                                        cells[curRowIndex][preColIndex].getChildren().clear();
                                        cells[curRowIndex][preColIndex].setUserData(null);
                                    });
                                    actionItem.setFinishFlag(true);
                                    ThreadSleepUtil.sleepSeconds(1L);
                                    break;
                                }
                                // 前面第二格是否空白
                                if (cells[curRowIndex][nextColIndex2].getChildren().isEmpty()) {
                                    move(actionItem, nextColIndex2);
                                    i--;
                                    ThreadSleepUtil.sleepSeconds(1L);
                                    continue;
                                } else {
                                    i = 0;
                                    ThreadSleepUtil.sleepSeconds(1L);
                                    continue;
                                }
                            } else {
                                i = 0;
                                ThreadSleepUtil.sleepSeconds(1L);
                                continue;
                            }
                        }
                    }

                    // 移动
                    move(actionItem, nextColIndex);
                    ThreadSleepUtil.sleepSeconds(1L);
                }
                if (!actionItem.isFinishFlag()) {
                    desktopContext.getNextActionDeque().add(actionItem);
                }
            }
            desktopContext.getActionDeque().addAll(desktopContext.getNextActionDeque());
            desktopContext.getNextActionDeque().clear();

            // 回合数+1
            Platform.runLater(() -> {
                desktopContext.getTurnCount().set(desktopContext.getTurnCount().get() + 1);
                desktopContext.getBeginTurn().setDisable(false);
                desktopContext.getSelectCard().setDisable(true);
                desktopContext.getEndTurn().setDisable(true);
            });
        }, 1L, TimeUnit.SECONDS);
    }

    private void move(ActionItem actionItem, int nextColIndex) {
        StackPane[][] cells = DesktopContext.getInstance().getCells();
        int curRowIndex = actionItem.getCurRowIndex();
        int preColIndex = actionItem.getCurColIndex();
        actionItem.setCurColIndex(nextColIndex);
        int curColIndex = actionItem.getCurColIndex();
        Platform.runLater(() -> {
            cells[curRowIndex][preColIndex].getChildren().clear();
            cells[curRowIndex][preColIndex].setUserData(null);
            Label label = new Label(actionItem.getUnitCard().getDescription());
            if (actionItem.isAiPlayer()) {
                label.setBackground(StyleConstants.RED_BACKGROUND);
            } else {
                label.setBackground(StyleConstants.PLAYER_UNIT_BACKGROUND);
            }
            label.setFont(StyleConstants.font16);
            cells[curRowIndex][curColIndex].getChildren().addAll(label);
            cells[curRowIndex][curColIndex].setUserData(actionItem);
        });
    }

    private int calcEqAddAttack(ActionItem actionItem) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        StackPane[][] cells = desktopContext.getCells();

        if (actionItem.isAiPlayer()) {
            if (!cells[actionItem.getCurRowIndex()][DesktopContext.getInstance().getAiPlayer().getEqColIndex()].getChildren().isEmpty()) {
                if (cells[actionItem.getCurRowIndex()][DesktopContext.getInstance().getAiPlayer().getEqColIndex()].getUserData() instanceof EquipmentCard equipmentCard) {
                    if (equipmentCard.getUnitType() == CardConstants.UnitType.ALL || equipmentCard.getUnitType() == actionItem.getUnitCard().getUnitType()) {
                        return equipmentCard.getAddAttack();
                    }
                }
            }
        } else {
            if (!cells[actionItem.getCurRowIndex()][DesktopContext.getInstance().getPeoplePlayer().getEqColIndex()].getChildren().isEmpty()) {
                if (cells[actionItem.getCurRowIndex()][DesktopContext.getInstance().getPeoplePlayer().getEqColIndex()].getUserData() instanceof EquipmentCard equipmentCard) {
                    if (equipmentCard.getUnitType() == CardConstants.UnitType.ALL || equipmentCard.getUnitType() == actionItem.getUnitCard().getUnitType()) {
                        return equipmentCard.getAddAttack();
                    }
                }
            }
        }
        return 0;
    }

    private void testGameOver() {
        DesktopContext desktopContext = DesktopContext.getInstance();

        if (desktopContext.getPeoplePlayer().getHp().get() <= 0) {
            if (!desktopContext.isGameOverFlag()) {
                Rectangle scrim = new Rectangle();
                scrim.widthProperty().bind(desktopContext.getRoot().widthProperty());
                scrim.heightProperty().bind(desktopContext.getRoot().heightProperty());
                scrim.setFill(Color.color(0, 0.5, 0, 0.5));
                BorderPane gameOverPane = new BorderPane();
                Label gameOverLabel = new Label("游戏结束！很遗憾输了！请关闭程序重新游玩。");
                gameOverLabel.setFont(StyleConstants.font24);
                gameOverPane.setCenter(gameOverLabel);
                desktopContext.getRoot().getChildren().addAll(scrim, gameOverPane);
            }
        } else if (desktopContext.getAiPlayer().getHp().get() <= 0) {
            if (!desktopContext.isGameOverFlag()) {
                Rectangle scrim = new Rectangle();
                scrim.widthProperty().bind(desktopContext.getRoot().widthProperty());
                scrim.heightProperty().bind(desktopContext.getRoot().heightProperty());
                scrim.setFill(Color.color(0, 0.5, 0, 0.5));
                BorderPane gameOverPane = new BorderPane();
                Label gameOverLabel = new Label("游戏结束！恭喜赢了！请关闭程序重新游玩。");
                gameOverLabel.setFont(StyleConstants.font24);
                gameOverPane.setCenter(gameOverLabel);

                desktopContext.getRoot().getChildren().addAll(scrim, gameOverPane);
            }
        }
    }

}
