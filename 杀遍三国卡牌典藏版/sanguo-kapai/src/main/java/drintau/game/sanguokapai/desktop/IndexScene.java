package drintau.game.sanguokapai.desktop;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public final class IndexScene extends Scene {

    public IndexScene() {
        super(createContent());
    }

    private static Parent createContent() {
        BorderPane indexRoot = new BorderPane();
        indexRoot.setBackground(StyleConstants.BLUE_BACKGROUND);

        return indexRoot;
    }
}
