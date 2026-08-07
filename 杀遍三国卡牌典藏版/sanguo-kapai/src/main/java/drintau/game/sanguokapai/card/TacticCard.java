package drintau.game.sanguokapai.card;

import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.util.RandomUtil;

/**
 * 计策卡
 */
public abstract class TacticCard extends AbstractCard {

    public abstract void exec(PlayerData playerData, int rowIndex);

    public int suggestRow(PlayerData playerData) {
        return RandomUtil.randomInt(DesktopContext.rows);
    }

}
