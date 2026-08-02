package drintau.game.sanguokapai.card;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnitCard extends AbstractCard {

    private String name;

    private CardConstants.UnitType unitType;

    private int level;

    private int speed;

    private int baseAttack;

    public UnitCard(String name, CardConstants.UnitType unitType, int level, int speed, int baseAttack) {
        setCardType(CardConstants.CardType.UNIT);
        this.name = name;
        this.unitType = unitType;
        this.level = level;
        this.speed = speed;
        this.baseAttack = baseAttack;
    }

    @Override
    public String toString() {
        return "UnitCard{" +
                "name='" + name + '\'' +
                ", unitType=" + unitType.displayName +
                ", level=" + level +
                ", speed=" + speed +
                ", baseAttack=" + baseAttack +
                '}';
    }

    @Override
    public String getDescription() {
        return """
            %s
            兵种：%s
            等级：%d
            速度：%d
            攻击力：%d
            """.formatted(name, unitType.displayName, level, speed, baseAttack);
    }
}
