package drintau.game.sanguokapai.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ToggleButton;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerData {

    // true:玩家，在左边，false：电脑，在右边
    private boolean flag;

    private IntegerProperty hp = new SimpleIntegerProperty(100);
    private IntegerProperty maxHp = new SimpleIntegerProperty(100);

    private ToggleButton selectCard;

}
