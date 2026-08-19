package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.EquipmentCard;
import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.desktop.event.BeginTurnEvent;
import drintau.game.sanguokapai.desktop.event.EndTurnEvent;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
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
