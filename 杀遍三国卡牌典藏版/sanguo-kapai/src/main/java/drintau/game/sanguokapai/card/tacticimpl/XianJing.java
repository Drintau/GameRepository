package drintau.game.sanguokapai.card.tacticimpl;

import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.ActionItem;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import drintau.game.sanguokapai.util.DaemonScheduler;
import drintau.game.sanguokapai.util.ThreadSleepUtil;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;

import java.util.concurrent.TimeUnit;

public class XianJing extends TacticCard {

    @Override
    public void exec(PlayerData playerData, int rowIndex) {
        DesktopContext.getInstance().getSelectCard().setDisable(true);
        DesktopContext.getInstance().getEndTurn().setDisable(true);
        DaemonScheduler.getInstance().submitOnceDelayTask(() -> {
            StackPane[][] cells = DesktopContext.getInstance().getCells();
            if (playerData.isAiFlag()) {
                // 电脑执行，从右到左
                for (int i = DesktopContext.aiPlayerUnitInitColIndex; i >= DesktopContext.peoplePlayerUnitInitColIndex; i--) {
                    StackPane cell = cells[rowIndex][i];
                    if (!cell.getChildren().isEmpty()) {
                        ActionItem targetCellActionItem = (ActionItem) cell.getUserData();
                        if (playerData.isAiFlag() != targetCellActionItem.isAiPlayer()) {
                            Platform.runLater(() -> {
                                cell.setBorder(StyleConstants.CELL_BORDER_ACTION);
                            });
                            targetCellActionItem.setDeadFlag(true);
                            ThreadSleepUtil.sleepSeconds(1L);
                            Platform.runLater(() -> {
                                cell.getChildren().clear();
                                cell.setUserData(null);
                                cell.setBorder(StyleConstants.CELL_BORDER_DEFAULT);
                            });
                            ThreadSleepUtil.sleepSeconds(1L);
                            break;
                        }
                    }
                }
                // 唤醒电脑行动线程
                synchronized (DesktopContext.getInstance().getAiActionLock()) {
                    DesktopContext.getInstance().getAiActionLock().notify();
                }
            } else {
                // 玩家执行，从左到右
                for (int i = DesktopContext.peoplePlayerUnitInitColIndex; i <= DesktopContext.aiPlayerUnitInitColIndex; i++) {
                    StackPane cell = cells[rowIndex][i];
                    if (!cell.getChildren().isEmpty()) {
                        ActionItem targetCellActionItem = (ActionItem) cell.getUserData();
                        if (playerData.isAiFlag() != targetCellActionItem.isAiPlayer()) {
                            Platform.runLater(() -> {
                                cell.setBorder(StyleConstants.CELL_BORDER_ACTION);
                            });
                            targetCellActionItem.setDeadFlag(true);
                            ThreadSleepUtil.sleepSeconds(1L);
                            Platform.runLater(() -> {
                                cell.getChildren().clear();
                                cell.setUserData(null);
                                cell.setBorder(StyleConstants.CELL_BORDER_DEFAULT);
                            });
                            ThreadSleepUtil.sleepSeconds(1L);
                            break;
                        }
                    }
                }
                // 玩家继续选择卡牌
                ThreadSleepUtil.sleepSeconds(1L);
                Platform.runLater(() -> {
                    DesktopContext.getInstance().getSelectCard().setDisable(false);
                    DesktopContext.getInstance().getEndTurn().setDisable(false);
                });
            }
        }, 1L, TimeUnit.SECONDS);
    }

    @Override
    public String getDescription() {
        return "陷阱：一行范围内，敌方最前面的一个单位会被消灭";
    }
}
