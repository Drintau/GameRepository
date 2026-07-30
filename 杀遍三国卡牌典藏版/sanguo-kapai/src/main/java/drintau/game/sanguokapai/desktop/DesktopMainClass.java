package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.desktop.event.BeginEvent;
import drintau.game.sanguokapai.util.DaemonScheduler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DesktopMainClass extends Application {

    @Override
    public void start(Stage stage) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        desktopContext.init();
        DaemonScheduler.getInstance();

        StackPane[][] cells = new StackPane[DesktopContext.rows][DesktopContext.cols];
        desktopContext.setCells(cells);

        GridPane gridPane = new GridPane();
        for (int row = 0; row < DesktopContext.rows; row++) {
            for (int col = 0; col < DesktopContext.cols; col++) {
                StackPane cell = new StackPane();
                cell.setBorder(StyleConstants.CELL_BORDER);
                cell.setPrefSize(100, 150);
                gridPane.add(cell, col ,row);
                cells[row][col] = cell;
            }
        }

        HBox hBox = new HBox();
        Button begin = new Button("下一步");
        begin.setOnAction(new BeginEvent());
        hBox.getChildren().addAll(begin);

        BorderPane borderPane = new BorderPane();
        borderPane.setBackground(StyleConstants.LIGHTGRAY_BACKGROUND);
        borderPane.setTop(hBox);
        borderPane.setCenter(gridPane);

        Scene scene = new Scene(borderPane);

        stage.setScene(scene);
        stage.setTitle("杀遍三国卡牌典藏版");
        stage.setWidth(1400);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();

    }

}
