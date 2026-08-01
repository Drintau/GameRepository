package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.UnitCard;

public final class SoldierData {

    public static final UnitCard QIANG_BING = new UnitCard("枪兵", CardConstants.UnitType.GUNNER, 1, 2, 4);

    public static final UnitCard SHAO_JI = new UnitCard("哨骑", CardConstants.UnitType.CAVALRY, 1, 2, 3);

    public static final UnitCard JIAN_DUN_JIA_SHI = new UnitCard("剑盾甲士", CardConstants.UnitType.ARMOR, 1, 2, 3);

    public static final UnitCard SHU_SHI = new UnitCard("术士", CardConstants.UnitType.MAGE, 1, 2, 3);

    public static final UnitCard DUN_BING = new UnitCard("盾兵", CardConstants.UnitType.SHIELD, 1, 2, 3);

    public static final UnitCard SHE_SHOU = new UnitCard("射手", CardConstants.UnitType.SHIELD, 1, 2, 3);


}
