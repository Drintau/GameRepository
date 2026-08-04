package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.UnitCard;

import java.util.List;

public final class SoldierData {

    public static final UnitCard CHANG_QIANG_BING = new UnitCard("长枪兵", CardConstants.UnitType.GUNNER, 1, 2, 4);
    public static final UnitCard SHUANG_JI_BING = new UnitCard("双戟兵", CardConstants.UnitType.GUNNER, 1, 2, 3);

    public static final UnitCard SHAO_JI = new UnitCard("哨骑", CardConstants.UnitType.CAVALRY, 2, 3, 4);
    public static final UnitCard XI_LIANG_YOU_QI = new UnitCard("西凉游骑", CardConstants.UnitType.CAVALRY, 3, 4, 5);
    public static final UnitCard JING_RUI_TIE_QI = new UnitCard("精锐铁骑", CardConstants.UnitType.CAVALRY, 4, 3, 6);

    public static final UnitCard JIAN_DUN_JIA_SHI = new UnitCard("剑盾甲士", CardConstants.UnitType.ARMOR, 3, 1, 5);
    public static final UnitCard CHANG_MAO_JIA_SHI = new UnitCard("长矛甲士", CardConstants.UnitType.ARMOR, 2, 2, 5);

    public static final UnitCard FANG_SHI = new UnitCard("方士", CardConstants.UnitType.MAGE, 2, 2, 3);
    public static final UnitCard GE_NV = new UnitCard("歌女", CardConstants.UnitType.MAGE, 2, 2, 3);

    public static final UnitCard PU_DAO_BING = new UnitCard("朴刀兵", CardConstants.UnitType.SHIELD, 1, 2, 3);
    public static final UnitCard ZHONG_DUN_BING = new UnitCard("重盾兵", CardConstants.UnitType.SHIELD, 2, 1, 4);

    public static final UnitCard GONG_JIAN_SHOU = new UnitCard("弓箭手", CardConstants.UnitType.SHOOTER, 2, 2, 3);
    public static final UnitCard LIAN_NU_SHOU = new UnitCard("连弩手", CardConstants.UnitType.SHOOTER, 2, 2, 4);

    public static List<UnitCard> getAllSoldiers() {
        return List.of(
                CHANG_QIANG_BING,
                SHUANG_JI_BING,
                SHAO_JI,
                XI_LIANG_YOU_QI,
                JING_RUI_TIE_QI,
                JIAN_DUN_JIA_SHI,
                CHANG_MAO_JIA_SHI,
                FANG_SHI,
                GE_NV,
                PU_DAO_BING,
                ZHONG_DUN_BING,
                GONG_JIAN_SHOU,
                LIAN_NU_SHOU
        );
    }

}
