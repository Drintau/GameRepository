package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.UnitCard;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ActionItem {

    // 归属阵营
    private boolean aiPlayer;

    // 行动完成标记
    private boolean finishFlag;

    // 位置
    private int curRowIndex;
    private int curColIndex;

    private UnitCard unitCard;

    public ActionItem(boolean aiPlayer, int curRowIndex, int curColIndex, UnitCard unitCard) {
        this.aiPlayer = aiPlayer;
        this.curRowIndex = curRowIndex;
        this.curColIndex = curColIndex;
        this.unitCard = unitCard;
    }
}
