package drintau.game.sanguokapai.desktop.scene;

import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class ShowAllCardScene extends Scene {

    public ShowAllCardScene() {
        super(createContent());
    }

    private static Parent createContent() {
        BorderPane showAllCardRoot = new BorderPane();
        showAllCardRoot.setBackground(StyleConstants.BLUE_BACKGROUND);
        showAllCardRoot.setPadding(new Insets(10));

        showAllCardRoot.setTop(DesktopContext.getInstance().getShowIndexSceneBtn());

        return showAllCardRoot;
    }

}
