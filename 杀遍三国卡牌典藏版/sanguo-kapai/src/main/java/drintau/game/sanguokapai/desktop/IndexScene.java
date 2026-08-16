package drintau.game.sanguokapai.desktop;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public final class IndexScene extends Scene {

    public IndexScene() {
        super(createContent());
    }

    private static Parent createContent() {
        DesktopContext desktopContext = DesktopContext.getInstance();

        BorderPane indexRoot = new BorderPane();
        indexRoot.setBackground(StyleConstants.BLUE_BACKGROUND);

        Button startGame = new Button("开始游戏");
        startGame.setFont(StyleConstants.font20);
        desktopContext.setStartGameBtn(startGame);

        Button showAllCard = new Button("卡牌一览");
        showAllCard.setFont(StyleConstants.font20);

        Button help = new Button("帮助说明");
        help.setFont(StyleConstants.font20);

        VBox indexCenter = new VBox(10);
        indexCenter.setAlignment(Pos.CENTER);
        indexCenter.setPadding(new Insets(10));
        indexCenter.getChildren().addAll(startGame, showAllCard, help);

        indexRoot.setCenter(indexCenter);

        return indexRoot;
    }
}
