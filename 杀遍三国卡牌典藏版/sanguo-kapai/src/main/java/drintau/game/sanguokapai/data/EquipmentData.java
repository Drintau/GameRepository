package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.EquipmentCard;

import java.util.List;

public final class EquipmentData {

    public static final EquipmentCard CHANG_QIANG = new EquipmentCard("长枪", CardConstants.UnitType.GUNNER, 0, 2);
    public static final EquipmentCard MA_KUI = new EquipmentCard("马盔", CardConstants.UnitType.CAVALRY, 0, 2);
    public static final EquipmentCard TENG_JIA = new EquipmentCard("藤甲", CardConstants.UnitType.ARMOR, 0, 2);
    public static final EquipmentCard YU_SHAN = new EquipmentCard("羽扇", CardConstants.UnitType.MAGE, 0, 2);
    public static final EquipmentCard REN_WANG_DUN = new EquipmentCard("仁王盾", CardConstants.UnitType.SHIELD, 0, 2);
    public static final EquipmentCard QI_LIN_GONG = new EquipmentCard("麒麟弓", CardConstants.UnitType.SHOOTER, 0, 2);
    public static final EquipmentCard YAN_YUE_DAO = new EquipmentCard("偃月刀", null, 0, 1);

    public static List<EquipmentCard> getAllEquipments() {
        return List.of(
                CHANG_QIANG,
                MA_KUI,
                TENG_JIA,
                YU_SHAN,
                REN_WANG_DUN,
                QI_LIN_GONG,
                YAN_YUE_DAO
        );
    }

}
