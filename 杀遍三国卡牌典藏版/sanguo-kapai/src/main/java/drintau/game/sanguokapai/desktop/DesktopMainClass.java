package drintau.game.sanguokapai.desktop;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DesktopMainClass extends Application {

    @Override
    public void start(Stage stage) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        Button endGame = new Button("返回首页");
        endGame.setFont(StyleConstants.font20);
        desktopContext.setEndGameBtn(endGame);

        IndexScene indexScene = new IndexScene(stage);

        desktopContext.getEndGameBtn().setOnAction(event -> {
            stage.setScene(indexScene);
        });

        stage.setScene(indexScene);
        stage.setTitle("杀遍三国卡牌典藏版");
        stage.setWidth(1400);
        stage.setHeight(680);
        stage.setResizable(false);
        stage.show();
    }

}
