package drintau.game.sanguokapai.desktop;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class UIComponentFactory {

    public static Rectangle createRectangle(StackPane root) {
        Rectangle scrim = new Rectangle();
        scrim.widthProperty().bind(root.widthProperty());
        scrim.heightProperty().bind(root.heightProperty());
        scrim.setFill(Color.color(0, 0.5, 0, 0.2));
        return scrim;
    }

}
