package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.EquipmentCard;

import java.util.List;

public final class EquipmentData {

    public static final EquipmentCard TIE_QIANG = new EquipmentCard("铁枪", CardConstants.UnitType.GUNNER, 0, 2);
    public static final EquipmentCard MA_KUI = new EquipmentCard("马盔", CardConstants.UnitType.CAVALRY, 0, 2);
    public static final EquipmentCard TENG_JIA = new EquipmentCard("藤甲", CardConstants.UnitType.ARMOR, 0, 2);
    public static final EquipmentCard YU_SHAN = new EquipmentCard("羽扇", CardConstants.UnitType.MAGE, 0, 2);
    public static final EquipmentCard GANG_DUN = new EquipmentCard("钢盾", CardConstants.UnitType.SHIELD, 0, 2);
    public static final EquipmentCard HUO_SHI = new EquipmentCard("火矢", CardConstants.UnitType.SHOOTER, 0, 2);
    public static final EquipmentCard YAN_YUE_DAO = new EquipmentCard("偃月刀", CardConstants.UnitType.ALL, 0, 1);
    public static final EquipmentCard CHI_TU = new EquipmentCard("赤兔", CardConstants.UnitType.ALL, 1, 0);

    public static List<EquipmentCard> getAllEquipments() {
        return List.of(
                TIE_QIANG,
                MA_KUI,
                TENG_JIA,
                YU_SHAN,
                GANG_DUN,
                HUO_SHI,
                YAN_YUE_DAO,
                CHI_TU
        );
    }

}
