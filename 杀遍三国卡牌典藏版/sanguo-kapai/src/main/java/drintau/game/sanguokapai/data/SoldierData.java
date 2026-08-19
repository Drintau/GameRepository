package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.UnitCard;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class SoldierData {

    // 定义数值范围，避免战力膨胀。
    // 每个兵种跨3个等级，1+2+3 或 2+3+4，器械统一3级
    // 战力范围：枪3-4，骑4-6，甲4-5，术2-3，盾2-3，射2-4，器5-6

    public static final UnitCard DUAN_QIANG_BING = new UnitCard("短枪兵", CardConstants.UnitType.GUNNER, 1, 2, 3);
    public static final UnitCard CHANG_QIANG_BING = new UnitCard("长枪兵", CardConstants.UnitType.GUNNER, 2, 2, 4);
    public static final UnitCard JING_YING_QIANG_BING = new UnitCard("精英枪兵", CardConstants.UnitType.GUNNER, 3, 2, 4);

    public static final UnitCard SHAO_JI = new UnitCard("哨骑", CardConstants.UnitType.CAVALRY, 2, 3, 4);
    public static final UnitCard XI_LIANG_YOU_QI = new UnitCard("西凉游骑", CardConstants.UnitType.CAVALRY, 3, 4, 5);
    public static final UnitCard JING_RUI_TIE_QI = new UnitCard("精锐铁骑", CardConstants.UnitType.CAVALRY, 4, 3, 6);

    public static final UnitCard CHANG_MAO_JIA_SHI = new UnitCard("长矛甲士", CardConstants.UnitType.ARMOR, 1, 2, 4);
    public static final UnitCard JIAN_DUN_JIA_SHI = new UnitCard("剑盾甲士", CardConstants.UnitType.ARMOR, 2, 1, 5);
    public static final UnitCard JING_YING_JIA_SHI = new UnitCard("精英甲士", CardConstants.UnitType.ARMOR, 3, 2, 5);

    public static final UnitCard GE_NV = new UnitCard("歌女", CardConstants.UnitType.MAGE, 1, 2, 2);
    public static final UnitCard FANG_SHI = new UnitCard("方士", CardConstants.UnitType.MAGE, 2, 2, 3);
    public static final UnitCard JING_YING_SHU_SHI = new UnitCard("精英术士", CardConstants.UnitType.MAGE, 3, 2, 3);

    public static final UnitCard PU_DAO_BING = new UnitCard("朴刀兵", CardConstants.UnitType.SHIELD, 1, 2, 2);
    public static final UnitCard ZHONG_DUN_BING = new UnitCard("重盾兵", CardConstants.UnitType.SHIELD, 2, 1, 3);
    public static final UnitCard JING_YING_DUN_BING = new UnitCard("精英盾兵", CardConstants.UnitType.SHIELD, 3, 2, 3);

    public static final UnitCard GONG_JIAN_SHOU = new UnitCard("弓箭手", CardConstants.UnitType.SHOOTER, 1, 2, 2);
    public static final UnitCard CHANG_GONG_SHOU = new UnitCard("长弓手", CardConstants.UnitType.SHOOTER, 2, 2, 3);
    public static final UnitCard ZHU_GE_BU_SHOU = new UnitCard("诸葛弩手", CardConstants.UnitType.SHOOTER, 3, 2, 4);

    public static final UnitCard YUN_TI = new UnitCard("云梯", CardConstants.UnitType.SIEGE, 3, 2, 5);
    public static final UnitCard CHONG_CHE = new UnitCard("冲车", CardConstants.UnitType.SIEGE, 3, 3, 5);
    public static final UnitCard TOU_SHI_CHE = new UnitCard("投石车", CardConstants.UnitType.SIEGE, 3, 1, 6);

    public static List<UnitCard> getAllSoldiers() {
        return List.of(
                DUAN_QIANG_BING,
                CHANG_QIANG_BING,
                JING_YING_QIANG_BING,

                SHAO_JI,
                XI_LIANG_YOU_QI,
                JING_RUI_TIE_QI,

                CHANG_MAO_JIA_SHI,
                JIAN_DUN_JIA_SHI,
                JING_YING_JIA_SHI,

                GE_NV,
                FANG_SHI,
                JING_YING_SHU_SHI,

                PU_DAO_BING,
                ZHONG_DUN_BING,
                JING_YING_DUN_BING,

                GONG_JIAN_SHOU,
                CHANG_GONG_SHOU,
                ZHU_GE_BU_SHOU,

                YUN_TI,
                CHONG_CHE,
                TOU_SHI_CHE
        );
    }

    public static Map<CardConstants.UnitType, List<UnitCard>> getUnitTypeCardListMap() {
        return getAllSoldiers().stream().collect(Collectors.groupingBy(UnitCard::getUnitType));
    }

}
