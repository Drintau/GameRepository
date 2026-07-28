package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.UnitCard;

public class UnitDataFactory {

    public UnitCard createQiangBing() {
        return new UnitCard("枪兵", CardConstants.UnitType.GUNNER, 1, 2, 4);
    }

    public UnitCard createDunBing() {
        return new UnitCard("盾兵", CardConstants.UnitType.SHIELD, 1, 2, 3);
    }

}
