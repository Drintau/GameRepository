package drintau.game.sanguokapai.desktop;

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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DesktopMainClass extends Application {

    @Override
    public void start(Stage stage) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        desktopContext.init();

        // 根节点
        StackPane root = new StackPane();

        // 棋盘
        BorderPane gameBoardPaneTop = new BorderPane();
        gameBoardPaneTop.setPadding(new Insets(10));
        gameBoardPaneTop.setPrefHeight(50);
        Label turnCountLabel = new Label();
        turnCountLabel.setFont(StyleConstants.font24);
        turnCountLabel.textProperty().bind(
                Bindings.format("回合数：%d", desktopContext.getTurnCount())
        );
        Label player1HpLabel = new Label();
        player1HpLabel.setFont(StyleConstants.font24);
        player1HpLabel.textProperty().bind(
                Bindings.format("玩家1 生命值：%d / %d", desktopContext.getPlayer1().getHp(), desktopContext.getPlayer1().getMaxHp())
        );
        Label player2HpLabel = new Label();
        player2HpLabel.setFont(StyleConstants.font24);
        player2HpLabel.textProperty().bind(
                Bindings.format("玩家2 生命值：%d / %d", desktopContext.getPlayer2().getHp(), desktopContext.getPlayer2().getMaxHp())
        );
        gameBoardPaneTop.setLeft(player1HpLabel);
        gameBoardPaneTop.setCenter(turnCountLabel);
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
                    if (desktopContext.getPlayer1().getSelectCard() != null) {
                        ToggleButton selectCard = desktopContext.getPlayer1().getSelectCard();
                        Object userData = selectCard.getUserData();
                        if (userData instanceof UnitCard unitCard) {
                            Label label = new Label(unitCard.getDescription());
                            label.setFont(StyleConstants.font16);
                            cell.getChildren().add(label);
                            desktopContext.getActionDeque().add(new ActionItem(true, finalRow, finalCol, unitCard));
                        }

                        desktopContext.getPlayer1().setSelectCard(null);
                        desktopContext.getCardList().remove(selectCard);
                        desktopContext.getCardSelectCenter().getChildren().remove(selectCard);
                    }
                });
                gridPane.add(cell, col ,row);
                cells[row][col] = cell;
            }
        }

        HBox gameBoardPaneBottom = new HBox(10);
        gameBoardPaneBottom.setAlignment(Pos.CENTER);
        gameBoardPaneBottom.setPadding(new Insets(10));
        Button beginTurn = new Button("开始回合");
        beginTurn.setFont(StyleConstants.font20);
        Button selectCard = new Button("选择卡牌");
        selectCard.setFont(StyleConstants.font20);
        selectCard.setDisable(true);
        Button endTurn = new Button("结束回合");
        endTurn.setFont(StyleConstants.font20);
        endTurn.setDisable(true);
        gameBoardPaneBottom.getChildren().addAll(beginTurn, selectCard, endTurn);
        desktopContext.setBeginTurn(beginTurn);
        desktopContext.setSelectCard(selectCard);
        desktopContext.setEndTurn(endTurn);

        beginTurn.setOnAction(new BeginTurnEvent());
        endTurn.setOnAction(new EndTurnEvent());

        BorderPane gameBoardPane = new BorderPane();
        gameBoardPane.setBackground(StyleConstants.BLUE_BACKGROUND);
        gameBoardPane.setTop(gameBoardPaneTop);
        gameBoardPane.setCenter(gridPane);
        gameBoardPane.setBottom(gameBoardPaneBottom);

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
        cardSelectTitle.setFont(StyleConstants.font24);
        cardSelectRoot.setTop(cardSelectTitle);
        BorderPane.setAlignment(cardSelectTitle, Pos.CENTER);

        HBox cardSelectCenter = new HBox(10);
        cardSelectCenter.setAlignment(Pos.CENTER);
        desktopContext.setCardSelectCenter(cardSelectCenter);

        cardSelectRoot.setCenter(cardSelectCenter);

        HBox cardSelectBottom = new HBox(10);
        cardSelectBottom.setAlignment(Pos.CENTER);
        Button cardSelectSureButton = new Button("确认");
        cardSelectSureButton.setFont(StyleConstants.font20);
        Button cardSelectCloseButton = new Button("关闭");
        cardSelectCloseButton.setFont(StyleConstants.font20);
        cardSelectBottom.getChildren().addAll(cardSelectSureButton, cardSelectCloseButton);
        cardSelectRoot.setBottom(cardSelectBottom);

        // 跳转
        cardSelectSureButton.setOnAction(e -> {
            if (desktopContext.getPlayer1().getSelectCard() != null) {
                root.getChildren().removeAll(scrim,cardSelectRoot);
            }
        });
        selectCard.setOnAction(e -> root.getChildren().addAll(scrim, cardSelectRoot));
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
