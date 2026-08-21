package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.desktop.scene.IndexScene;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DesktopMainClass extends Application {

    @Override
    public void start(Stage stage) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        desktopContext.setStage(stage);

        IndexScene indexScene = new IndexScene(stage);

        desktopContext.setIndexScene(indexScene);
        Button showIndexSceneBtn = new Button("返回首页");
        showIndexSceneBtn.setFont(StyleConstants.font20);
        showIndexSceneBtn.setOnAction(event -> {
            stage.setScene(indexScene);
        });
        desktopContext.setShowIndexSceneBtn(showIndexSceneBtn);

        stage.setScene(indexScene);
        stage.setTitle("杀遍三国卡牌典藏版");
        stage.setWidth(1400);
        stage.setHeight(680);
        stage.setResizable(false);
        stage.show();
    }

}
