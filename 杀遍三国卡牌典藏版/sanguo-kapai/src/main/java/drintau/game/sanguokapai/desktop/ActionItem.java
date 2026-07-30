package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.UnitCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ActionItem {

    // 阵营：玩家1、玩家2
    private boolean player1;

    // 存活标记
    private boolean flag = true;

    // 位置
    private int curRowIndex;
    private int curColIndex;

    private UnitCard unitCard;

    public ActionItem(boolean player1, int curRowIndex, int curColIndex, UnitCard unitCard) {
        this.player1 = player1;
        this.curRowIndex = curRowIndex;
        this.curColIndex = curColIndex;
        this.unitCard = unitCard;
    }
}
