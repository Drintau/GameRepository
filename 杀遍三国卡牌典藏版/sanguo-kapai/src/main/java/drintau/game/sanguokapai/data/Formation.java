package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.EquipmentCard;
import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.card.UnitCard;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 阵型
 */
@Setter
@Getter
public class Formation {

    private String name;

    private List<UnitCard> soldierList;
    private List<UnitCard> heroList;
    private List<EquipmentCard> equipmentList;
    private List<TacticCard> tacticList;

    public Formation(String name) {
        this.name = name;
    }
}
