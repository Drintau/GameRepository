package drintau.game.sanguokapai.card.tacticimpl;

import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.DesktopContext;

/**
 * 暗箭伤人：减少敌方5点生命值
 */
public class AnJianShangRen extends TacticCard {

    @Override
    public void exec(PlayerData playerData) {
        if (playerData.isAiFlag()) {
            PlayerData peoplePlayer = DesktopContext.getInstance().getPeoplePlayer();
            peoplePlayer.getHp().set(peoplePlayer.getHp().get() - 5);
        } else {
            PlayerData aiPlayer = DesktopContext.getInstance().getAiPlayer();
            aiPlayer.getHp().set(aiPlayer.getHp().get() - 5);
        }
    }

    @Override
    public String getDescription() {
        return "暗箭伤人：减少敌方5点生命值";
    }
}
