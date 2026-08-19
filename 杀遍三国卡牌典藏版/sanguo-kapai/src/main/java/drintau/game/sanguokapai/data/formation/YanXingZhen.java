package drintau.game.sanguokapai.data.formation;

import drintau.game.sanguokapai.card.CardConstants;

public final class YanXingZhen extends AbstractFormation{

    public YanXingZhen() {
        super(FormationConstants.YAN_XING_ZHEN, 50);

        getUnitTypeCountMap().put(CardConstants.UnitType.GUNNER, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.CAVALRY, 12);
        getUnitTypeCountMap().put(CardConstants.UnitType.ARMOR, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.MAGE, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.SHOOTER, 12);
        getUnitTypeCountMap().put(CardConstants.UnitType.SIEGE, 6);

        init();
    }

}
