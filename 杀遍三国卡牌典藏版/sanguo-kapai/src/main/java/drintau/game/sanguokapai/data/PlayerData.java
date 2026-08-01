package drintau.game.sanguokapai.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ToggleButton;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerData {

    private boolean flag;

    private IntegerProperty hp = new SimpleIntegerProperty(100);
    private IntegerProperty maxHp = new SimpleIntegerProperty(100);

    private ToggleButton selectCard;

}
