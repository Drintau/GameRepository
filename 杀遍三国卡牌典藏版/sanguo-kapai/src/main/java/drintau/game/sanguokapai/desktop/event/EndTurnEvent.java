package drintau.game.sanguokapai.desktop.event;

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
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EndTurnEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        DesktopContext desktopContext = DesktopContext.getInstance();

        desktopContext.getBeginTurn().setDisable(false);
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
                        int lowerHP = unitCard.getBaseAttack();
                        Platform.runLater(() -> {
                            desktopContext.getPeoplePlayer().getHp().set(desktopContext.getPeoplePlayer().getHp().get() - lowerHP);
                            cells[curRowIndex][preColIndex].getChildren().clear();
                            cells[curRowIndex][preColIndex].setUserData(null);
                        });
                        actionItem.setFinishFlag(true);
                        ThreadSleepUtil.sleepSeconds(1L);
                        break;
                    } else if (!actionItem.isAiPlayer() && desktopContext.getAiPlayer().beAttack(nextColIndex)) {
                        int lowerHP = unitCard.getBaseAttack();
                        Platform.runLater(() -> {
                            desktopContext.getAiPlayer().getHp().set(desktopContext.getAiPlayer().getHp().get() - lowerHP);
                            cells[curRowIndex][preColIndex].getChildren().clear();
                            cells[curRowIndex][preColIndex].setUserData(null);
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
                            int u1Attack = actionItem.getUnitCard().getBaseAttack();
                            int u2Attack = targetCellActionItem.getUnitCard().getBaseAttack();
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
                                    int lowerHP = unitCard.getBaseAttack();
                                    Platform.runLater(() -> {
                                        desktopContext.getPeoplePlayer().getHp().set(desktopContext.getPeoplePlayer().getHp().get() - lowerHP);
                                        cells[curRowIndex][preColIndex].getChildren().clear();
                                        cells[curRowIndex][preColIndex].setUserData(null);
                                    });
                                    actionItem.setFinishFlag(true);
                                    ThreadSleepUtil.sleepSeconds(1L);
                                    break;
                                } else if (!actionItem.isAiPlayer() && desktopContext.getAiPlayer().beAttack(nextColIndex2)) {
                                    int lowerHP = unitCard.getBaseAttack();
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

}
