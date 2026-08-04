package drintau.game.sanguokapai.card.tacticimpl;

import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.ActionItem;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.util.DaemonScheduler;
import drintau.game.sanguokapai.util.RandomUtil;
import drintau.game.sanguokapai.util.ThreadSleepUtil;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;

import java.util.concurrent.TimeUnit;

public class LuoLei extends TacticCard {

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
                        int randomInt = RandomUtil.randomInt(2);
                        if (randomInt < 1) {
                            targetCellActionItem.setDeadFlag(true);
                            Platform.runLater(() -> {
                                cell.getChildren().clear();
                                cell.setUserData(null);
                            });
                            ThreadSleepUtil.sleepSeconds(1L);
                        }
                    }
                }
            }
            ThreadSleepUtil.sleepSeconds(1L);
            Platform.runLater(() -> {
                DesktopContext.getInstance().getSelectCard().setDisable(false);
            });
        }, 1L, TimeUnit.SECONDS);
    }

    @Override
    public String getDescription() {
        return "落雷：敌方一行的每个单位有50%概率被消灭";
    }

}
