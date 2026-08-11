package drintau.game.sanguokapai.desktop.event;

import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import drintau.game.sanguokapai.desktop.UIComponentFactory;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;

public class GameOverEvent {

    public void testGameOver() {
        DesktopContext desktopContext = DesktopContext.getInstance();

        if (desktopContext.getPeoplePlayer().getHp().get() <= 0) {
            if (!desktopContext.isGameOverFlag()) {
                Rectangle scrim = UIComponentFactory.createRectangle(desktopContext.getRoot());
                BorderPane gameOverPane = new BorderPane();
                Label gameOverLabel = new Label("游戏结束！很遗憾输了！请关闭程序重新游玩。");
                gameOverLabel.setBackground(StyleConstants.WHITE_BACKGROUND);
                gameOverLabel.setFont(StyleConstants.font24);
                gameOverPane.setCenter(gameOverLabel);
                desktopContext.setGameOverFlag(true);
                Platform.runLater(() -> {
                    desktopContext.getRoot().getChildren().addAll(scrim, gameOverPane);
                });
            }
        } else if (desktopContext.getAiPlayer().getHp().get() <= 0) {
            if (!desktopContext.isGameOverFlag()) {
                Rectangle scrim = UIComponentFactory.createRectangle(desktopContext.getRoot());
                BorderPane gameOverPane = new BorderPane();
                Label gameOverLabel = new Label("游戏结束！恭喜赢了！请关闭程序重新游玩。");
                gameOverLabel.setBackground(StyleConstants.WHITE_BACKGROUND);
                gameOverLabel.setFont(StyleConstants.font24);
                gameOverPane.setCenter(gameOverLabel);
                desktopContext.setGameOverFlag(true);
                Platform.runLater(() -> {
                    desktopContext.getRoot().getChildren().addAll(scrim, gameOverPane);
                });
            }
        }
    }

}
