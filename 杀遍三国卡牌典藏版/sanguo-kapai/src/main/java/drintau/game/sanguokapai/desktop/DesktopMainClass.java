package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.data.HeroData;
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
        BorderPane cardSelectRoot = new BorderPane();
        cardSelectRoot.setPadding(new Insets(10));
        cardSelectRoot.setBackground(StyleConstants.WHITE_BACKGROUND);
        cardSelectRoot.setPrefWidth(600);
        cardSelectRoot.setPrefHeight(300);
        cardSelectRoot.setMinSize(600, 300);
        cardSelectRoot.setMaxSize(600, 300);

        Label cardSelectTitle = new Label("选择卡牌");
        cardSelectTitle.setFont(font20);
        cardSelectRoot.setTop(cardSelectTitle);
        BorderPane.setAlignment(cardSelectTitle, Pos.CENTER);

        ToggleGroup cardSelectGroup = new ToggleGroup();
        HBox cardSelectCenter = new HBox(10);
        cardSelectCenter.setAlignment(Pos.CENTER);
        for (int i = 0; i < 5; i++) {
            ToggleButton cardBtn = new ToggleButton(HeroData.GUAN_YU.getName());
            cardBtn.setUserData(HeroData.GUAN_YU);
            cardBtn.setToggleGroup(cardSelectGroup);
            cardSelectCenter.getChildren().add(cardBtn);
        }
        cardSelectGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                ToggleButton selected = (ToggleButton) newVal;
                log.warn("{}", selected.getUserData());
            }
        });

        cardSelectRoot.setCenter(cardSelectCenter);

        HBox cardSelectBottom = new HBox(10);
        cardSelectBottom.setAlignment(Pos.CENTER);
        Button cardSelectSureButton = new Button("确认");
        cardSelectSureButton.setFont(font16);
        Button cardSelectCloseButton = new Button("关闭");
        cardSelectCloseButton.setFont(font16);
        cardSelectBottom.getChildren().addAll(cardSelectSureButton, cardSelectCloseButton);
        cardSelectRoot.setBottom(cardSelectBottom);

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
