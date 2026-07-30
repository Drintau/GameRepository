package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.UnitCard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class ActionItem {

    // 阵营：玩家1、玩家2
    private boolean player1;

    // 位置
    private int curRowIndex;
    private int curColIndex;

    private UnitCard unitCard;

}
