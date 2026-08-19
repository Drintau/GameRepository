package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.formation.AbstractFormation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FormationData {

    private final Map<String, AbstractFormation> formationMap =  new HashMap<>();

    private final Map<CardConstants.UnitType, List<UnitCard>>  unitTypeCardListMap = SoldierData.getUnitTypeCardListMap();

    public FormationData() {
    }
}
