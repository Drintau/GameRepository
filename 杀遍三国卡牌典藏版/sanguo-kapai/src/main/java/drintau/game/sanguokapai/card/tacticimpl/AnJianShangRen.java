package drintau.game.sanguokapai.card.tacticimpl;

import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.util.DaemonScheduler;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

public class AnJianShangRen extends TacticCard {

    @Override
    public void exec(PlayerData playerData, int rowIndex) {
        Platform.runLater(() -> {
            if (playerData.isAiFlag()) {
                PlayerData peoplePlayer = DesktopContext.getInstance().getPeoplePlayer();
                peoplePlayer.getHp().set(peoplePlayer.getHp().get() - 5);
            } else {
                PlayerData aiPlayer = DesktopContext.getInstance().getAiPlayer();
                aiPlayer.getHp().set(aiPlayer.getHp().get() - 5);
            }
            DesktopContext.getInstance().getGameOverEvent().testGameOver();
        });
        if (playerData.isAiFlag()) {
            DaemonScheduler.getInstance().submitOnceDelayTask(() -> {
                synchronized (DesktopContext.getInstance().getAiActionLock()) {
                    DesktopContext.getInstance().getAiActionLock().notify();
                }
            }, 1L, TimeUnit.SECONDS);
        }
    }

    @Override
    public String getDescription() {
        return "暗箭伤人：减少敌方5点生命值";
    }
}
