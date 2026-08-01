package drintau.game.sanguokapai.desktop;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DesktopMainClass extends Application {

    @Override
    public void start(Stage stage) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        desktopContext.init();

        Font font16 = Font.font(16);
        Font font20 = Font.font(20);

        // 根节点
        StackPane root = new StackPane();

        // 棋盘
        BorderPane gameBoardPaneTop = new BorderPane();
        gameBoardPaneTop.setPadding(new Insets(10));
        gameBoardPaneTop.setPrefHeight(50);
        Label player1HpLabel = new Label();
        player1HpLabel.setFont(font20);
        player1HpLabel.textProperty().bind(
                Bindings.format("玩家1 生命值：%d / %d", desktopContext.getPlayer1().getHp(), desktopContext.getPlayer1().getMaxHp())
        );
        Label player2HpLabel = new Label();
        player2HpLabel.setFont(font20);
        player2HpLabel.textProperty().bind(
                Bindings.format("玩家2 生命值：%d / %d", desktopContext.getPlayer2().getHp(), desktopContext.getPlayer2().getMaxHp())
        );

        Button nextTurn = new Button("下一回合");
        nextTurn.setFont(font16);
        gameBoardPaneTop.setLeft(player1HpLabel);
        gameBoardPaneTop.setCenter(nextTurn);
        gameBoardPaneTop.setRight(player2HpLabel);

        StackPane[][] cells = new StackPane[DesktopContext.rows][DesktopContext.cols];
        desktopContext.setCells(cells);
        GridPane gridPane = new GridPane();
        for (int row = 0; row < DesktopContext.rows; row++) {
            for (int col = 0; col < DesktopContext.cols; col++) {
                StackPane cell = new StackPane();
                cell.setBorder(StyleConstants.CELL_BORDER);
                cell.setBackground(StyleConstants.LIGHTGRAY_BACKGROUND);
                cell.setPrefSize(100, 150);
                int finalRow = row;
                int finalCol = col;
                cell.setOnMouseClicked(e -> {
                    log.warn("点击格子：row={},col={}", finalRow, finalCol);
                });
                gridPane.add(cell, col ,row);
                cells[row][col] = cell;
            }
        }

        BorderPane gameBoardPane = new BorderPane();
        gameBoardPane.setBackground(StyleConstants.BLUE_BACKGROUND);
        gameBoardPane.setTop(gameBoardPaneTop);
        gameBoardPane.setCenter(gridPane);

        // 遮盖层
        Rectangle scrim = new Rectangle();
        scrim.widthProperty().bind(root.widthProperty());
        scrim.heightProperty().bind(root.heightProperty());
        scrim.setFill(Color.color(0, 0.5, 0, 0.2));

        // 选择卡牌
        VBox cardSelectRoot = new VBox(10);
        cardSelectRoot.setBackground(StyleConstants.WHITE_BACKGROUND);
        cardSelectRoot.setPrefWidth(600);
        cardSelectRoot.setPrefHeight(300);
        cardSelectRoot.setMinSize(600, 300);
        cardSelectRoot.setMaxSize(600, 300);
        cardSelectRoot.setAlignment(Pos.CENTER);
        cardSelectRoot.getChildren().add(new Label("选择卡牌"));

        ToggleGroup cardSelectGroup = new ToggleGroup();
        ToggleButton cardBtn1 = new ToggleButton("卡牌A");
        ToggleButton cardBtn2 = new ToggleButton("卡牌B");
        ToggleButton cardBtn3 = new ToggleButton("卡牌C");
        ToggleButton cardBtn4 = new ToggleButton("卡牌C");
        ToggleButton cardBtn5 = new ToggleButton("卡牌C");
        cardBtn1.setToggleGroup(cardSelectGroup);
        cardBtn2.setToggleGroup(cardSelectGroup);
        cardBtn3.setToggleGroup(cardSelectGroup);
        cardBtn4.setToggleGroup(cardSelectGroup);
        cardBtn5.setToggleGroup(cardSelectGroup);
        cardSelectGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selected = (ToggleButton) newVal;
            }
        });
        HBox cardSelectHBox = new HBox(10);
        cardSelectHBox.setAlignment(Pos.CENTER);
        cardSelectHBox.getChildren().addAll(cardBtn1, cardBtn2, cardBtn3, cardBtn4, cardBtn5);
        cardSelectRoot.getChildren().add(cardSelectHBox);

        Button cardSelectSureButton = new Button("确认");
        Button cardSelectCloseButton = new Button("关闭");
        cardSelectRoot.getChildren().addAll(cardSelectSureButton,cardSelectCloseButton);

        // 跳转
        nextTurn.setOnAction(e -> root.getChildren().addAll(scrim, cardSelectRoot));
        cardSelectCloseButton.setOnAction(e -> root.getChildren().removeAll(scrim,cardSelectRoot));

        root.getChildren().addAll(gameBoardPane);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("杀遍三国卡牌典藏版");
        stage.setWidth(1400);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();

    }

}
