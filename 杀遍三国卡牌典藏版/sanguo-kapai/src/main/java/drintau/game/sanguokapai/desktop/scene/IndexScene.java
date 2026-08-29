package drintau.game.sanguokapai.desktop.scene;

import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public final class IndexScene extends Scene {

    public IndexScene(Stage stage) {
        super(createContent(stage));
    }

    private static Parent createContent(Stage stage) {
        BorderPane indexRoot = new BorderPane();
        indexRoot.setBackground(StyleConstants.BLUE_BACKGROUND);

        Button startGame = new Button("开始游戏");
        startGame.setFont(StyleConstants.font20);
        startGame.setOnAction(event -> {
            stage.setScene(new PlayGameScene());
        });

        Button showAllFormation = new Button("阵型一览");
        showAllFormation.setFont(StyleConstants.font20);
        showAllFormation.setOnAction(event -> {
            stage.setScene(new ShowAllFormationScene());
        });

        Button showAllCard = new Button("卡牌一览");
        showAllCard.setFont(StyleConstants.font20);
        showAllCard.setOnAction(event -> {
            stage.setScene(new ShowAllCardScene());
        });

        Button help = new Button("游玩说明");
        help.setFont(StyleConstants.font20);
        help.setOnAction(event -> {
            stage.setScene(new HelpScene(stage));
        });

        VBox indexCenter = new VBox(10);
        indexCenter.setAlignment(Pos.CENTER);
        indexCenter.setPadding(new Insets(10));
        indexCenter.getChildren().addAll(startGame, showAllFormation, showAllCard, help);

        indexRoot.setCenter(indexCenter);

        return indexRoot;
    }
}
