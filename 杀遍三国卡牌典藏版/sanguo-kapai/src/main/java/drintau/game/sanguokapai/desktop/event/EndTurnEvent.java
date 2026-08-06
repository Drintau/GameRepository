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
        desktopContext.getPeoplePlayer().setSelectCard(null);

        // 移动单位
        DaemonScheduler.getInstance().submitOnceDelayTask(() -> {
            ArrayDeque<ActionItem> actionDeque = desktopContext.getActionDeque();

            // 冲锋模式
            boolean chargeMode = false;

            while (!actionDeque.isEmpty()) {
                ActionItem actionItem = actionDeque.pollFirst();
                if (actionItem == null || actionItem.isDeadFlag()) {
                    continue;
                }

                chargeMode = checkChargeMode(chargeMode, actionItem);

                // 移动力
                int addSpeed = calcEqAddSpeed(actionItem);
                actionItem.setAddSpeed(addSpeed);
                actionItem.setCurSpeed(actionItem.getUnitCard().getSpeed() + actionItem.getAddSpeed());
                // 当前行
                int curRowIndex = actionItem.getCurRowIndex();
                // 目标列
                int nextColIndex = actionItem.getCurColIndex();

                // 移动
                for (int i = actionItem.getCurSpeed(); i > 0 ; i--) {
                    if (actionItem.isDeadFlag()) {
                        break;
                    }
                    if (actionItem.isMoveFinishFlag()) {
                        break;
                    }
                    if (chargeMode) {
                        i = DesktopContext.maxSpeed;
                    }
                    if (actionItem.isAiPlayer()) {
                        nextColIndex = nextColIndex - 1;
                    } else {
                        nextColIndex = nextColIndex + 1;
                    }
                    boolean moveSuccessFlag = move(actionItem, curRowIndex, nextColIndex);
                    if (moveSuccessFlag) {
                        actionItem.setCurColIndex(nextColIndex);
                    }
                }
                if (!actionItem.isDeadFlag()) {
                    actionItem.setMoveFinishFlag(false);
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

    // 返回true，当前位置有变化（即真实移动）；返回false，当前位置无变化（未移动，但对应任务可能已经完成）。返回值影响actionItem的curColIndex是否要变化
    private boolean move(ActionItem actionItem, int curRowIndex, int nextColIndex) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        StackPane[][] cells = desktopContext.getCells();
        StackPane cell = cells[curRowIndex][nextColIndex];

        if (cell.getChildren().isEmpty()) {
            // 目标格子空白：如果是敌方本阵，攻入；否则移动到该格子
            if (actionItem.isAiPlayer() && desktopContext.getPeoplePlayer().beAttack(nextColIndex)) {
                int eqAddAttack = calcEqAddAttack(actionItem);
                actionItem.setAddAttack(eqAddAttack);
                actionItem.setCurAttack(actionItem.getUnitCard().getBaseAttack() + actionItem.getAddAttack());
                int lowerHP = actionItem.getCurAttack();
                Platform.runLater(() -> {
                    desktopContext.getPeoplePlayer().getHp().set(desktopContext.getPeoplePlayer().getHp().get() - lowerHP);
                    cells[curRowIndex][actionItem.getCurColIndex()].getChildren().clear();
                    cells[curRowIndex][actionItem.getCurColIndex()].setUserData(null);
                    desktopContext.getGameOverEvent().testGameOver();
                });
                actionItem.setDeadFlag(true);
                actionItem.setMoveFinishFlag(true);
                ThreadSleepUtil.sleepSeconds(1L);
                return false;
            } else if (!actionItem.isAiPlayer() && desktopContext.getAiPlayer().beAttack(nextColIndex)) {
                int eqAddAttack = calcEqAddAttack(actionItem);
                actionItem.setAddAttack(eqAddAttack);
                actionItem.setCurAttack(actionItem.getUnitCard().getBaseAttack() + actionItem.getAddAttack());
                int lowerHP = actionItem.getCurAttack();
                Platform.runLater(() -> {
                    desktopContext.getAiPlayer().getHp().set(desktopContext.getAiPlayer().getHp().get() - lowerHP);
                    cells[curRowIndex][actionItem.getCurColIndex()].getChildren().clear();
                    cells[curRowIndex][actionItem.getCurColIndex()].setUserData(null);
                    desktopContext.getGameOverEvent().testGameOver();
                });
                actionItem.setDeadFlag(true);
                actionItem.setMoveFinishFlag(true);
                ThreadSleepUtil.sleepSeconds(1L);
                return false;
            }
            // 移动
            moveUI(actionItem, cell);
            ThreadSleepUtil.sleepSeconds(1L);
            return true;
        } else {
            // 目标格子不空白：如果是敌方单位，战斗；如果是己方单位，再判断下一格，移动力消耗加1
            ActionItem targetCellActionItem = (ActionItem) cell.getUserData();
            if (actionItem.isAiPlayer() != targetCellActionItem.isAiPlayer()) {
                // 加成战力归0
                actionItem.setAddAttack(0);
                targetCellActionItem.setAddAttack(0);
                // 装备加成
                int actionItemEqAddAttack = calcEqAddAttack(actionItem);
                int targetCellEqAddAttack = calcEqAddAttack(targetCellActionItem);
                actionItem.setAddAttack(actionItem.getAddAttack() + actionItemEqAddAttack);
                targetCellActionItem.setAddAttack(targetCellActionItem.getAddAttack() + targetCellEqAddAttack);
                // 属性克制
                CardConstants.UnitType actionItemBeatUnitType = desktopContext.ADVANTAGE_MAP.get(actionItem.getUnitCard().getUnitType());
                CardConstants.UnitType targetCellBeatUnitType = desktopContext.ADVANTAGE_MAP.get(targetCellActionItem.getUnitCard().getUnitType());
                if (actionItemBeatUnitType == targetCellActionItem.getUnitCard().getUnitType()) {
                    actionItem.setAddAttack(actionItem.getAddAttack() + 1);
                    targetCellActionItem.setAddAttack(targetCellActionItem.getAddAttack() - 1);
                }
                if (targetCellBeatUnitType == actionItem.getUnitCard().getUnitType()) {
                    actionItem.setAddAttack(actionItem.getAddAttack() - 1);
                    targetCellActionItem.setAddAttack(targetCellActionItem.getAddAttack() + 1);
                }
                // 最终战力
                actionItem.setCurAttack(actionItem.getUnitCard().getBaseAttack() + actionItem.getAddAttack());
                targetCellActionItem.setCurAttack(targetCellActionItem.getUnitCard().getBaseAttack() + targetCellActionItem.getAddAttack());
                // 战斗界面
                Platform.runLater(() -> {
                    showAttackUI(actionItem, targetCellActionItem);
                });
                synchronized (desktopContext.getBattleLock()) {
                    try {
                        desktopContext.getBattleLock().wait();
                    } catch (Exception e) {
                        log.error("战斗出错", e);
                        throw new RuntimeException(e);
                    }
                }
                ThreadSleepUtil.sleepSeconds(1L);
                // 击败判定
                int u1Attack = actionItem.getCurAttack();
                int u2Attack = targetCellActionItem.getCurAttack();
                if (u1Attack > u2Attack) {
                    // u2死了
                    targetCellActionItem.setDeadFlag(true);
                    int removeColIndex = targetCellActionItem.getCurColIndex();
                    Platform.runLater(() -> {
                        cells[curRowIndex][removeColIndex].getChildren().clear();
                        cells[curRowIndex][removeColIndex].setUserData(null);
                    });
                    // 提前标记，后面的移动还是正常执行一次的
                    actionItem.setMoveFinishFlag(true);
                    // 如果该格子是敌方的本阵，则不能移入
                    if (desktopContext.getPeoplePlayer().beAttack(nextColIndex) || desktopContext.getAiPlayer().beAttack(nextColIndex)) {
                        ThreadSleepUtil.sleepSeconds(1L);
                        return false;
                    }
                    moveUI(actionItem, cell);
                    ThreadSleepUtil.sleepSeconds(1L);
                    return true;
                } else if (u1Attack < u2Attack) {
                    actionItem.setDeadFlag(true);
                    actionItem.setMoveFinishFlag(true);
                    int removeColIndex = actionItem.getCurColIndex();
                    Platform.runLater(() -> {
                        cells[curRowIndex][removeColIndex].getChildren().clear();
                        cells[curRowIndex][removeColIndex].setUserData(null);
                    });
                    ThreadSleepUtil.sleepSeconds(1L);
                    return false;
                } else {
                    actionItem.setDeadFlag(true);
                    actionItem.setMoveFinishFlag(true);
                    targetCellActionItem.setDeadFlag(true);
                    int removeColIndex1 = actionItem.getCurColIndex();
                    int removeColIndex2 = targetCellActionItem.getCurColIndex();
                    Platform.runLater(() -> {
                        cells[curRowIndex][removeColIndex1].getChildren().clear();
                        cells[curRowIndex][removeColIndex1].setUserData(null);
                        cells[curRowIndex][removeColIndex2].getChildren().clear();
                        cells[curRowIndex][removeColIndex2].setUserData(null);
                    });
                    ThreadSleepUtil.sleepSeconds(1L);
                    return false;
                }
            } else {
                // 要跳过下一格再移动时，actionItem 里面的列不能变，其他情况要变
                return false;
            }
        }
    }

    private void moveUI(ActionItem actionItem, StackPane cell) {
        StackPane[][] cells = DesktopContext.getInstance().getCells();
        Platform.runLater(() -> {
            cells[actionItem.getCurRowIndex()][actionItem.getCurColIndex()].getChildren().clear();
            cells[actionItem.getCurRowIndex()][actionItem.getCurColIndex()].setUserData(null);
            Label label = new Label(actionItem.getUnitCard().getDescription());
            label.setFont(StyleConstants.font16);
            if (actionItem.isAiPlayer()) {
                label.setBackground(StyleConstants.RED_BACKGROUND);
            } else {
                label.setBackground(StyleConstants.PLAYER_UNIT_BACKGROUND);
            }
            cell.getChildren().addAll(label);
            cell.setUserData(actionItem);
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

    private int calcEqAddSpeed(ActionItem actionItem) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        StackPane[][] cells = desktopContext.getCells();

        if (actionItem.isAiPlayer()) {
            if (!cells[actionItem.getCurRowIndex()][DesktopContext.getInstance().getAiPlayer().getEqColIndex()].getChildren().isEmpty()) {
                if (cells[actionItem.getCurRowIndex()][DesktopContext.getInstance().getAiPlayer().getEqColIndex()].getUserData() instanceof EquipmentCard equipmentCard) {
                    if (equipmentCard.getUnitType() == CardConstants.UnitType.ALL) {
                        return equipmentCard.getAddSpeed();
                    }
                }
            }
        } else {
            if (!cells[actionItem.getCurRowIndex()][DesktopContext.getInstance().getPeoplePlayer().getEqColIndex()].getChildren().isEmpty()) {
                if (cells[actionItem.getCurRowIndex()][DesktopContext.getInstance().getPeoplePlayer().getEqColIndex()].getUserData() instanceof EquipmentCard equipmentCard) {
                    if (equipmentCard.getUnitType() == CardConstants.UnitType.ALL) {
                        return equipmentCard.getAddSpeed();
                    }
                }
            }
        }
        return 0;
    }

    private void showAttackUI(ActionItem actionItem, ActionItem targetCellActionItem) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        Label peoplePlayerUnit;
        Label aiPlayerUnit;
        if (actionItem.isAiPlayer()) {
            aiPlayerUnit = new Label(actionItem.getAttackInfo());
            peoplePlayerUnit = new Label(targetCellActionItem.getAttackInfo());
        } else {
            peoplePlayerUnit = new Label(actionItem.getAttackInfo());
            aiPlayerUnit = new Label(targetCellActionItem.getAttackInfo());
        }
        peoplePlayerUnit.setFont(StyleConstants.font16);
        peoplePlayerUnit.setBackground(StyleConstants.PLAYER_UNIT_BACKGROUND);
        aiPlayerUnit.setFont(StyleConstants.font16);
        aiPlayerUnit.setBackground(StyleConstants.RED_BACKGROUND);
        desktopContext.getAttackRoot().setLeft(peoplePlayerUnit);
        desktopContext.getAttackRoot().setRight(aiPlayerUnit);
        desktopContext.getRoot().getChildren().add(desktopContext.getAttackRoot());
    }

    // 判断是否开启冲锋模式
    private boolean checkChargeMode(boolean lastChargeMode, ActionItem actionItem) {
        if (!lastChargeMode) {
            StackPane[][] cells = DesktopContext.getInstance().getCells();
            for (int rowIndex = 0; rowIndex < DesktopContext.rows; rowIndex++) {
                for (int colIndex = DesktopContext.peoplePlayerUnitInitColIndex; colIndex <= DesktopContext.aiPlayerUnitInitColIndex; colIndex++) {
                    StackPane cell = cells[rowIndex][colIndex];
                    if (!cell.getChildren().isEmpty()) {
                        ActionItem targetCellActionItem = (ActionItem) cell.getUserData();
                        if (actionItem.isAiPlayer() != targetCellActionItem.isAiPlayer()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
