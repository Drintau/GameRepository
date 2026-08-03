package drintau.game.sanguokapai.data;

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

    private IntegerProperty hp = new SimpleIntegerProperty(100);
    private IntegerProperty maxHp = new SimpleIntegerProperty(100);

    private ToggleButton selectCard;
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
