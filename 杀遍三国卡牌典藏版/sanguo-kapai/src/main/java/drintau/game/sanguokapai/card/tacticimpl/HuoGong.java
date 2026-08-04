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

public class HuoGong extends TacticCard {

    @Override
    public void exec(PlayerData playerData, int rowIndex) {
        DesktopContext.getInstance().getSelectCard().setDisable(true);
        DaemonScheduler.getInstance().submitOnceDelayTask(() -> {
            StackPane[][] cells = DesktopContext.getInstance().getCells();
            for (int i = DesktopContext.peoplePlayerUnitInitColIndex; i <= DesktopContext.aiPlayerUnitInitColIndex; i++) {
                StackPane cell = cells[rowIndex][i];
                if (!cell.getChildren().isEmpty()) {
                    ActionItem targetCellActionItem = (ActionItem) cell.getUserData();
                    if (playerData.isAiFlag() != targetCellActionItem.isAiPlayer()) {
                        Platform.runLater(() -> {
                            cell.setBorder(StyleConstants.CELL_BORDER_ACTION);
                        });
                        ThreadSleepUtil.sleepSeconds(1L);
                        if (targetCellActionItem.getUnitCard().getLevel() < 3) {
                            targetCellActionItem.setDeadFlag(true);
                            Platform.runLater(() -> {
                                cell.getChildren().clear();
                                cell.setUserData(null);
                                cell.setBorder(StyleConstants.CELL_BORDER_DEFAULT);
                            });
                        } else {
                            Platform.runLater(() -> {
                                cell.setBorder(StyleConstants.CELL_BORDER_DEFAULT);
                            });
                        }
                        ThreadSleepUtil.sleepSeconds(1L);
                    }
                }
            }
            if (!playerData.isAiFlag()) {
                ThreadSleepUtil.sleepSeconds(1L);
                Platform.runLater(() -> {
                    DesktopContext.getInstance().getSelectCard().setDisable(false);
                });
            } else {
                synchronized (DesktopContext.getInstance().getAiActionLock()) {
                    DesktopContext.getInstance().getAiActionLock().notify();
                }
            }
        }, 1L, TimeUnit.SECONDS);
    }

    @Override
    public String getDescription() {
        return "火攻：消灭敌方一行等级小于3的单位（等级1和等级2）";
    }

}
