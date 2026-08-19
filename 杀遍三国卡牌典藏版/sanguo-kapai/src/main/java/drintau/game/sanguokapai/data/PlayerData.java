package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.data.formation.AbstractFormation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ToggleButton;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerData {

    // 电脑标记
    private boolean aiFlag;

    // 装备列下标
    private int eqColIndex;
    // 出兵列下表，也是大本营下标
    private int unitInitColIndex;

    // 生命值
    private IntegerProperty hp = new SimpleIntegerProperty(100);
    private IntegerProperty maxHp = new SimpleIntegerProperty(100);

    // 阵型
    private AbstractFormation formation;
    // 伤亡计数
    private IntegerProperty deadCount = new SimpleIntegerProperty(0);
    private IntegerProperty maxDeadCount;

    private ToggleButton curCard;
    private boolean curTurnPutUnitCardFlag;
    private boolean curTurnPutEqCardFlag;
    private boolean curTurnPutTacticCardFlag;

    public boolean beAttack(int colIndex) {
        if (aiFlag) {
            return colIndex >= unitInitColIndex;
        } else {
            return colIndex <= unitInitColIndex;
        }
    }

}
