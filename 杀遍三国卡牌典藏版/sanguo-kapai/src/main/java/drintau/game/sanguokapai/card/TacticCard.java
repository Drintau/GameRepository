package drintau.game.sanguokapai.card;

import drintau.game.sanguokapai.data.PlayerData;

/**
 * 计策卡
 */
public abstract class TacticCard extends AbstractCard {

    public abstract void exec(PlayerData playerData);

}
