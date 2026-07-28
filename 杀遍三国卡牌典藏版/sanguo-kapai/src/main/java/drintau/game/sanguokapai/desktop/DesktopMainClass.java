package drintau.game.sanguokapai.desktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DesktopMainClass extends Application {

    @Override
    public void start(Stage stage) {
        int rows = 3; // 行
        int cols = 14; // 列
        StackPane[][] cells = new StackPane[rows][cols];

        Border cellBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));
        Background background = Background.fill(Color.web("#2196F3", 1.0));

        GridPane gridPane = new GridPane();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                StackPane cell = new StackPane();
                cell.setBorder(cellBorder);
                cell.setPrefSize(100, 150);
                gridPane.add(cell, col ,row);
                cells[row][col] = cell;
            }
        }

        cells[1][5].setBackground(background);

        BorderPane borderPane = new BorderPane();
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
