package drintau.game.sanguokapai.data.formation;

import drintau.game.sanguokapai.card.CardConstants;

public final class ChongEZhen extends AbstractFormation{

    public ChongEZhen() {
        super(FormationConstants.CHONG_E_ZHEN, 50);

        getUnitTypeCountMap().put(CardConstants.UnitType.GUNNER, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.CAVALRY, 9);
        getUnitTypeCountMap().put(CardConstants.UnitType.ARMOR, 6);
        getUnitTypeCountMap().put(CardConstants.UnitType.SHOOTER, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.SIEGE, 21);

        init();
    }

}
