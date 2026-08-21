package drintau.game.sanguokapai.desktop.scene;

import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HelpScene extends Scene {

    public HelpScene(Stage stage) {
        super(createContent(stage));
    }

    private static Parent createContent(Stage stage) {
        DesktopContext desktopContext = DesktopContext.getInstance();

        BorderPane helpRoot = new BorderPane();
        helpRoot.setBackground(StyleConstants.BLUE_BACKGROUND);
        helpRoot.setPadding(new Insets(10));

        helpRoot.setBottom(desktopContext.getShowIndexSceneBtn());
        BorderPane.setAlignment(desktopContext.getShowIndexSceneBtn(), Pos.CENTER);

        return helpRoot;
    }

}
