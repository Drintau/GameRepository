package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.UnitCard;

import java.util.List;

public final class HeroData {

    public static final UnitCard GUAN_YU = new UnitCard("关羽", CardConstants.UnitType.CAVALRY, 5, 4, 9);
    public static final UnitCard ZHANG_FEI = new UnitCard("张飞", CardConstants.UnitType.GUNNER, 5, 3, 8);
    public static final UnitCard MA_CHAO = new UnitCard("马超", CardConstants.UnitType.CAVALRY, 5, 5, 8);
    public static final UnitCard ZHAO_YUN = new UnitCard("赵云", CardConstants.UnitType.GUNNER, 5, 3, 8);
    public static final UnitCard HUANG_ZHONG = new UnitCard("黄忠", CardConstants.UnitType.SHOOTER, 5, 2, 7);
    public static final UnitCard ZHU_GE_LIANG = new UnitCard("诸葛亮", CardConstants.UnitType.MAGE, 5, 2, 6);

    public static final UnitCard ZHOU_TAI = new UnitCard("周泰", CardConstants.UnitType.ARMOR, 5, 3, 7);
    public static final UnitCard SUN_SHANG_XIANG = new UnitCard("孙尚香", CardConstants.UnitType.SHOOTER, 5, 2, 5);

    public static final UnitCard SI_MA_YI = new UnitCard("司马懿", CardConstants.UnitType.MAGE, 5, 2, 6);
    public static final UnitCard CAO_REN = new UnitCard("曹仁", CardConstants.UnitType.SHIELD, 5, 2, 7);

    public static final UnitCard LV_BU = new UnitCard("吕布", CardConstants.UnitType.CAVALRY, 5, 4, 10);

    public static List<UnitCard> getAllHeroes() {
        return List.of(
                GUAN_YU,
                ZHANG_FEI,
                MA_CHAO,
                ZHAO_YUN,
                HUANG_ZHONG,
                ZHU_GE_LIANG,

                ZHOU_TAI,
                SUN_SHANG_XIANG,

                SI_MA_YI,
                CAO_REN,

                LV_BU
        );
    }

}