package drintau.game.sanguokapai.card;

import lombok.Getter;
import lombok.Setter;

/**
 * 装备卡
 */
@Getter
@Setter
public class EquipmentCard extends AbstractCard {

    private String name;

    private CardConstants.UnitType unitType;

    private int addSpeed;

    private int addAttack;

    public EquipmentCard(String name, CardConstants.UnitType unitType, int addSpeed, int addAttack) {
        setCardType(CardConstants.CardType.EQUIPMENT);
        this.name = name;
        this.unitType = unitType;
        this.addSpeed = addSpeed;
        this.addAttack = addAttack;
    }

    @Override
    public String getDescription() {
        return """
            %s
            生效兵种：%s
            加速度：%d
            加战力：%d
            """.formatted(name, unitType.displayName, addSpeed, addAttack);
    }

}
