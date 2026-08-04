package drintau.game.sanguokapai.card;

import drintau.game.sanguokapai.data.PlayerData;

/**
 * 计策卡
 */
public abstract class TacticCard extends AbstractCard {

    // 返回是否需要等待 tactic 锁，只对电脑有用
    public abstract boolean exec(PlayerData playerData, int rowIndex);

}
