package drintau.game.sanguokapai.data.formation;

import drintau.game.sanguokapai.card.CardConstants;

public final class YanXingZhen extends AbstractFormation{

    public YanXingZhen() {
        super("雁形阵", 50);

        getUnitTypeCountMap().put(CardConstants.UnitType.GUNNER, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.CAVALRY, 12);
        getUnitTypeCountMap().put(CardConstants.UnitType.ARMOR, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.MAGE, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.SHOOTER, 12);
        getUnitTypeCountMap().put(CardConstants.UnitType.SIEGE, 6);

        init();
    }

}
