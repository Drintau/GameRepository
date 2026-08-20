package drintau.game.sanguokapai.data.formation;

import drintau.game.sanguokapai.card.CardConstants;

public final class ChangSheZhen extends AbstractFormation{

    public ChangSheZhen() {
        super(FormationConstants.CHANG_SHE_ZHEN, 50);

        getUnitTypeCountMap().put(CardConstants.UnitType.GUNNER, 21);
        getUnitTypeCountMap().put(CardConstants.UnitType.ARMOR, 9);
        getUnitTypeCountMap().put(CardConstants.UnitType.SHIELD, 6);
        getUnitTypeCountMap().put(CardConstants.UnitType.SHOOTER, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.SIEGE, 3);

        init();
    }
}
