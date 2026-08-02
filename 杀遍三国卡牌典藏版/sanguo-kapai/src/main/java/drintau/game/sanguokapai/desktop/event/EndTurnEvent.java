package drintau.game.sanguokapai.desktop.event;

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
                if (!actionItem.isFlag()) {
                    continue;
                }

                //前进
                for (int i = actionItem.getUnitCard().getSpeed(); i > 0 ; i--) {
                    // 当前行
                    int curRowIndex = actionItem.getCurRowIndex();
                    // 当前列，移动时就是前一列了
                    int preColIndex = actionItem.getCurColIndex();
                    int nextColIndex;
                    if (actionItem.isPlayer1()) {
                        nextColIndex = actionItem.getCurColIndex() + 1;
                    } else {
                        nextColIndex = actionItem.getCurColIndex() - 1;
                    }
                    // 到达终点
                    if (nextColIndex < DesktopContext.moveMinColIndex || nextColIndex > DesktopContext.moveMaxColIndex) {
                        Platform.runLater(() -> {
                            cells[curRowIndex][preColIndex].getChildren().clear();
                            cells[curRowIndex][preColIndex].setUserData(null);
                        });
                        break;
                    }

                    // 碰撞
                    if (!cells[curRowIndex][nextColIndex].getChildren().isEmpty()) {
                        ActionItem targetCellActionItem = (ActionItem) cells[curRowIndex][nextColIndex].getUserData();
                        if (actionItem.isPlayer1() != targetCellActionItem.isPlayer1()) {
                            log.warn("战斗");
                            int u1Attack = actionItem.getUnitCard().getBaseAttack();
                            int u2Attack = targetCellActionItem.getUnitCard().getBaseAttack();
                            if (u1Attack > u2Attack) {
                                // u2死了
                                targetCellActionItem.setFlag(false);
                                int removeColIndex = targetCellActionItem.getCurColIndex();
                                Platform.runLater(() -> {
                                    cells[curRowIndex][removeColIndex].getChildren().clear();
                                    cells[curRowIndex][removeColIndex].setUserData(null);
                                });
                                move(actionItem, nextColIndex);
                            } else if (u1Attack < u2Attack) {
                                actionItem.setFlag(false);
                                int removeColIndex = actionItem.getCurColIndex();
                                Platform.runLater(() -> {
                                    cells[curRowIndex][removeColIndex].getChildren().clear();
                                    cells[curRowIndex][removeColIndex].setUserData(null);
                                });
                            } else {
                                actionItem.setFlag(false);
                                targetCellActionItem.setFlag(false);
                                int removeColIndex1 = actionItem.getCurColIndex();
                                int removeColIndex2 = targetCellActionItem.getCurColIndex();
                                Platform.runLater(() -> {
                                    cells[curRowIndex][removeColIndex1].getChildren().clear();
                                    cells[curRowIndex][removeColIndex1].setUserData(null);
                                    cells[curRowIndex][removeColIndex2].getChildren().clear();
                                    cells[curRowIndex][removeColIndex2].setUserData(null);
                                });
                            }
                        }
                        break;
                    }

                    // 移动
                    move(actionItem, nextColIndex);
                    ThreadSleepUtil.sleepSeconds(1L);
                }
                if (actionItem.isFlag()) {
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
            label.setFont(StyleConstants.font16);
            cells[curRowIndex][curColIndex].getChildren().addAll(label);
            cells[curRowIndex][curColIndex].setUserData(actionItem);
        });
    }

}
