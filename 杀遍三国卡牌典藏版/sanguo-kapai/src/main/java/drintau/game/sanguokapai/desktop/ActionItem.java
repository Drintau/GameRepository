package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.UnitCard;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ActionItem {

    // 归属阵营
    private boolean aiPlayer;

    // 存活标记
    private boolean deadFlag;
    // 行动完成标记
    private boolean moveFinishFlag;

    // 位置
    private int curRowIndex;
    private int curColIndex;

    private UnitCard unitCard;

    // 增加速度
    private int addSpeed;
    // 当前速度
    private int curSpeed;

    // 增加战力
    private int addAttack;
    // 当前战力
    private int curAttack;

    public ActionItem(boolean aiPlayer, int curRowIndex, int curColIndex, UnitCard unitCard) {
        this.aiPlayer = aiPlayer;
        this.curRowIndex = curRowIndex;
        this.curColIndex = curColIndex;
        this.unitCard = unitCard;
    }

    public String getAttackInfo() {
        return """
            %s
            兵种：%s
            等级：%d
            速度：%d + %d = %d
            战力：%d + %d = %d
            """.formatted(unitCard.getName(), unitCard.getUnitType().displayName, unitCard.getLevel(), unitCard.getSpeed(), addSpeed, curSpeed, unitCard.getBaseAttack(), addAttack, curAttack);
    }

}
