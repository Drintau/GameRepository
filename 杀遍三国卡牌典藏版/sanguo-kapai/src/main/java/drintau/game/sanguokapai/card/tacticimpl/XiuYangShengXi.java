package drintau.game.sanguokapai.card.tacticimpl;

import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.util.DaemonScheduler;
import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

public class XiuYangShengXi extends TacticCard {

    @Override
    public void exec(PlayerData playerData, int rowIndex) {
        if (playerData.isAiFlag()) {
            PlayerData aiPlayer = DesktopContext.getInstance().getAiPlayer();
            int nowHp = aiPlayer.getHp().get() + 5;
            if (nowHp > aiPlayer.getMaxHp().get()) {
                nowHp = aiPlayer.getMaxHp().get();
            }
            int finalNowHp = nowHp;
            Platform.runLater(() -> {
                aiPlayer.getHp().set(finalNowHp);
            });
        } else {
            PlayerData peoplePlayer = DesktopContext.getInstance().getPeoplePlayer();
            int nowHp = peoplePlayer.getHp().get() + 5;
            if (nowHp > peoplePlayer.getMaxHp().get()) {
                nowHp = peoplePlayer.getMaxHp().get();
            }
            int finalNowHp = nowHp;
            Platform.runLater(() -> {
                peoplePlayer.getHp().set(finalNowHp);
            });
        }
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
        return "休养生息：增加己方5点生命值";
    }

}
