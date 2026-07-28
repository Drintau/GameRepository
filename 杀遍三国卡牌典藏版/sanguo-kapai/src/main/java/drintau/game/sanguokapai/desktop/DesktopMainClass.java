package drintau.game.sanguokapai.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DesktopMainClass extends Application {

    @Override
    public void start(Stage stage) {

        BorderPane borderPane = new BorderPane();

        Scene scene = new Scene(borderPane);

        stage.setScene(scene);
        stage.setTitle("杀遍三国卡牌典藏版");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();

    }

}
