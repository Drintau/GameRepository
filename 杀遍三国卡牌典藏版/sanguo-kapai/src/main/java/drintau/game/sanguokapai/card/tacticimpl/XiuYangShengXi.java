package drintau.game.sanguokapai.card.tacticimpl;

import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.DesktopContext;

public class XiuYangShengXi extends TacticCard {

    @Override
    public void exec(PlayerData playerData) {
        if (playerData.isAiFlag()) {
            PlayerData aiPlayer = DesktopContext.getInstance().getAiPlayer();
            aiPlayer.getHp().set(aiPlayer.getHp().get() + 5);
        } else {
            PlayerData peoplePlayer = DesktopContext.getInstance().getPeoplePlayer();
            peoplePlayer.getHp().set(peoplePlayer.getHp().get() + 5);
        }
    }

    @Override
    public String getDescription() {
        return "休养生息：增加己方5点生命值";
    }

}
