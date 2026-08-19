package drintau.game.sanguokapai.data.formation;

import drintau.game.sanguokapai.card.CardConstants;

public final class ChangSheZhen extends AbstractFormation{

    public ChangSheZhen() {
        super("长蛇阵", 50);

        getUnitTypeCountMap().put(CardConstants.UnitType.GUNNER, 12);
        getUnitTypeCountMap().put(CardConstants.UnitType.ARMOR, 12);
        getUnitTypeCountMap().put(CardConstants.UnitType.MAGE, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.SHIELD, 6);
        getUnitTypeCountMap().put(CardConstants.UnitType.SHOOTER, 3);
        getUnitTypeCountMap().put(CardConstants.UnitType.SIEGE, 6);

        init();
    }
}
