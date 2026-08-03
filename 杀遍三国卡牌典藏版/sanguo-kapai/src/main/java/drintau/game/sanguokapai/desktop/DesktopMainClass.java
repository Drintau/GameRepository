package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.EquipmentCard;
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
        desktopContext.ruleInit();

        // 根节点
        StackPane root = new StackPane();
        desktopContext.setRoot(root);

        // 棋盘界面
        BorderPane gameBoardPaneTop = new BorderPane();
        gameBoardPaneTop.setPadding(new Insets(10));
        gameBoardPaneTop.setPrefHeight(50);
        Label turnCountLabel = new Label();
        turnCountLabel.setFont(StyleConstants.font24);
        turnCountLabel.textProperty().bind(
                Bindings.format("回合数：%d", desktopContext.getTurnCount())
        );
        Label peoplePlayerHpLabel = new Label();
        peoplePlayerHpLabel.setFont(StyleConstants.font24);
        desktopContext.setPeoplePlayerHpLabel(peoplePlayerHpLabel);
        Label aiPlayerHpLabel = new Label();
        aiPlayerHpLabel.setFont(StyleConstants.font24);
        desktopContext.setAiPlayerHpLabel(aiPlayerHpLabel);
        gameBoardPaneTop.setLeft(peoplePlayerHpLabel);
        gameBoardPaneTop.setCenter(turnCountLabel);
        gameBoardPaneTop.setRight(aiPlayerHpLabel);

        StackPane[][] cells = new StackPane[DesktopContext.rows][DesktopContext.cols];
        desktopContext.setCells(cells);
        GridPane gridPane = new GridPane();
        for (int row = 0; row < DesktopContext.rows; row++) {
            for (int col = 0; col < DesktopContext.cols; col++) {
                StackPane cell = new StackPane();
                cell.setBorder(StyleConstants.CELL_BORDER);
                if (col == DesktopContext.peoplePlayerEqColIndex || col == DesktopContext.aiPlayerEqColIndex) {
                    cell.setBackground(StyleConstants.WHITE_BACKGROUND);
                    Label label = new Label("装备区");
                    label.setFont(StyleConstants.font16);
                    cell.getChildren().add(label);
                } else if (col == DesktopContext.peoplePlayer1UnitInitColIndex){
                    cell.setBackground(StyleConstants.PLAYER_UNIT_BACKGROUND);
                } else if (col == DesktopContext.aiPlayerUnitInitColIndex) {
                    cell.setBackground(StyleConstants.RED_BACKGROUND);
                } else {
                    cell.setBackground(StyleConstants.LIGHTGRAY_BACKGROUND);
                }
                cell.setPrefSize(100, 150);
                int finalRow = row;
                int finalCol = col;
                cell.setOnMouseClicked(e -> {
                    if (desktopContext.getPeoplePlayer().getSelectCard() != null) {
                        boolean putFlag = false;
                        ToggleButton selectCard = desktopContext.getPeoplePlayer().getSelectCard();
                        Object userData = selectCard.getUserData();
                        if (userData instanceof UnitCard unitCard) {
                            if (finalCol == DesktopContext.peoplePlayer1UnitInitColIndex) {
                                cell.getChildren().clear();
                                cell.setUserData(null);
                                ActionItem actionItem = new ActionItem(false, finalRow, finalCol, unitCard);
                                Label label = new Label(unitCard.getDescription());
                                label.setBackground(StyleConstants.PLAYER_UNIT_BACKGROUND);
                                label.setFont(StyleConstants.font16);
                                cell.getChildren().add(label);
                                cell.setUserData(actionItem);
                                desktopContext.getActionDeque().add(actionItem);
                                putFlag = true;
                                desktopContext.getPeoplePlayer().setCurTurnPutUnitCardFlag(true);
                            }
                        } else if (userData instanceof EquipmentCard equipmentCard) {
                            if (finalCol == DesktopContext.peoplePlayerEqColIndex) {
                                cell.getChildren().clear();
                                cell.setUserData(null);
                                Label label = new Label(equipmentCard.getDescription());
                                label.setBackground(StyleConstants.WHITE_BACKGROUND);
                                label.setFont(StyleConstants.font16);
                                cell.getChildren().add(label);
                                cell.setUserData(equipmentCard);
                                putFlag = true;
                                desktopContext.getPeoplePlayer().setCurTurnPutEqCardFlag(true);
                            }
                        }

                        if (putFlag) {
                            desktopContext.getPeoplePlayer().setSelectCard(null);
                            desktopContext.getCardList().remove(selectCard);
                            desktopContext.getCardSelectCenter().getChildren().remove(selectCard);
                        }
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
        Button endTurn = new Button("结束操作");
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

        // 战斗界面
        BorderPane attackRoot = new BorderPane();
        attackRoot.setPadding(new Insets(10));
        attackRoot.setBackground(StyleConstants.WHITE_BACKGROUND);
        attackRoot.setPrefWidth(300);
        attackRoot.setPrefHeight(220);
        attackRoot.setMinSize(300, 220);
        attackRoot.setMaxSize(300, 220);

        Label attackTitle = new Label("战斗");
        attackTitle.setFont(StyleConstants.font24);
        attackRoot.setTop(attackTitle);
        BorderPane.setAlignment(attackTitle, Pos.CENTER);

        HBox attackBottom = new HBox(10);
        attackBottom.setAlignment(Pos.CENTER);
        Button attackSureButton = new Button("确认");
        attackSureButton.setFont(StyleConstants.font20);
        attackSureButton.setOnAction(e -> {
            desktopContext.getRoot().getChildren().remove(desktopContext.getAttackRoot());
            Object battleLock = desktopContext.getBattleLock();
            synchronized (battleLock) {
                battleLock.notify();
            }
        });
        attackBottom.getChildren().addAll(attackSureButton);
        attackRoot.setBottom(attackBottom);

        desktopContext.setAttackRoot(attackRoot);

        // 跳转
        cardSelectSureButton.setOnAction(e -> {
            if (desktopContext.getPeoplePlayer().getSelectCard() != null) {
                Object userData = desktopContext.getPeoplePlayer().getSelectCard().getUserData();
                if (userData instanceof UnitCard) {
                    if (!desktopContext.getPeoplePlayer().isCurTurnPutUnitCardFlag()) {
                        root.getChildren().removeAll(scrim,cardSelectRoot);
                    }
                } else if (userData instanceof EquipmentCard) {
                    if (!desktopContext.getPeoplePlayer().isCurTurnPutEqCardFlag()) {
                        root.getChildren().removeAll(scrim,cardSelectRoot);
                    }
                }
            }
        });
        selectCard.setOnAction(e -> root.getChildren().addAll(scrim, cardSelectRoot));
        cardSelectCloseButton.setOnAction(e -> root.getChildren().removeAll(scrim,cardSelectRoot));

        root.getChildren().addAll(gameBoardPane);
        Scene scene = new Scene(root);

        // 玩家初始化
        desktopContext.playerInit();

        stage.setScene(scene);
        stage.setTitle("杀遍三国卡牌典藏版");
        stage.setWidth(1400);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

}
