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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DesktopMainClass extends Application {

    @Override
    public void start(Stage stage) {
        DesktopContext desktopContext = DesktopContext.getInstance();

        // 根节点
        StackPane root = new StackPane();
        desktopContext.init(root);

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
        peoplePlayerHpLabel.textProperty().bind(
                Bindings.format("玩家 生命值：%d / %d", desktopContext.getPeoplePlayer().getHp(), desktopContext.getPeoplePlayer().getMaxHp())
        );
        Label aiPlayerHpLabel = new Label();
        aiPlayerHpLabel.setFont(StyleConstants.font24);
        aiPlayerHpLabel.textProperty().bind(
                Bindings.format("电脑 生命值：%d / %d", desktopContext.getAiPlayer().getHp(), desktopContext.getAiPlayer().getMaxHp())
        );
        gameBoardPaneTop.setLeft(peoplePlayerHpLabel);
        gameBoardPaneTop.setCenter(turnCountLabel);
        gameBoardPaneTop.setRight(aiPlayerHpLabel);
        Label helpTextLabel = new Label("克制关系：枪->骑->甲->术->盾->射->枪，器械无克制关系");
        helpTextLabel.setFont(StyleConstants.font24);
        gameBoardPaneTop.setTop(helpTextLabel);
        BorderPane.setAlignment(helpTextLabel, Pos.CENTER);

        StackPane[][] cells = new StackPane[DesktopContext.rows][DesktopContext.cols];
        desktopContext.setCells(cells);
        GridPane gridPane = new GridPane();
        for (int row = 0; row < DesktopContext.rows; row++) {
            for (int col = 0; col < DesktopContext.cols; col++) {
                StackPane cell = new StackPane();
                cell.setBorder(StyleConstants.CELL_BORDER_DEFAULT);
                if (col == DesktopContext.peoplePlayerEqColIndex || col == DesktopContext.aiPlayerEqColIndex) {
                    cell.setBackground(StyleConstants.WHITE_BACKGROUND);
                    Label label = new Label("装备区");
                    label.setFont(StyleConstants.font16);
                    cell.getChildren().add(label);
                } else if (col == DesktopContext.peoplePlayerUnitInitColIndex){
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
                            if (finalCol == DesktopContext.peoplePlayerUnitInitColIndex) {
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
                        } else if (userData instanceof TacticCard tacticCard) {
                            if (finalCol == DesktopContext.peoplePlayerUnitInitColIndex) {
                                BorderPane execTacticRoot = UIComponentFactory.createExecTacticRoot(tacticCard, finalRow, desktopContext.getPeoplePlayer());
                                desktopContext.getRoot().getChildren().addAll(desktopContext.getScrim(), execTacticRoot);
                                putFlag = true;
                                desktopContext.getPeoplePlayer().setCurTurnPutTacticCardFlag(true);
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
        Rectangle scrim = UIComponentFactory.createRectangle(root);

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
            synchronized (desktopContext.getBattleLock()) {
                desktopContext.getBattleLock().notify();
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
                } else if (userData instanceof TacticCard) {
                    if (!desktopContext.getPeoplePlayer().isCurTurnPutTacticCardFlag()) {
                        root.getChildren().removeAll(scrim,cardSelectRoot);
                    }
                }
            }
        });
        selectCard.setOnAction(e -> root.getChildren().addAll(scrim, cardSelectRoot));
        cardSelectCloseButton.setOnAction(e -> root.getChildren().removeAll(scrim,cardSelectRoot));

        root.getChildren().addAll(gameBoardPane);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("杀遍三国卡牌典藏版");
        stage.setWidth(1400);
        stage.setHeight(650);
        stage.setResizable(false);
        stage.show();
    }

}
