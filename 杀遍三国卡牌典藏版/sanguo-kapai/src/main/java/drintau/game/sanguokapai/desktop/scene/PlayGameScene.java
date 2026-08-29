package drintau.game.sanguokapai.desktop.scene;

import drintau.game.sanguokapai.card.EquipmentCard;
import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.desktop.ActionItem;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import drintau.game.sanguokapai.desktop.UIComponentFactory;
import drintau.game.sanguokapai.desktop.event.BeginTurnEvent;
import drintau.game.sanguokapai.desktop.event.EndTurnEvent;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;

public final class PlayGameScene extends Scene {

    public PlayGameScene() {
        super(createContent());
    }

    private static Parent createContent() {
        DesktopContext desktopContext = DesktopContext.getInstance();
        // 根节点
        StackPane playGameRoot = new StackPane();
        desktopContext.init(playGameRoot);

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
        Label peoplePlayerDeadCountLabel = new Label();
        peoplePlayerDeadCountLabel.setFont(StyleConstants.font24);
        peoplePlayerDeadCountLabel.textProperty().bind(
                Bindings.format("%s 伤亡数：%d / %d", desktopContext.getPeoplePlayer().getFormation().getName(), desktopContext.getPeoplePlayer().getDeadCount(), desktopContext.getPeoplePlayer().getMaxDeadCount())
        );
        VBox peoplePlayerInfo = new VBox();
        peoplePlayerInfo.getChildren().addAll(peoplePlayerHpLabel, peoplePlayerDeadCountLabel);

        Label aiPlayerHpLabel = new Label();
        aiPlayerHpLabel.setFont(StyleConstants.font24);
        aiPlayerHpLabel.textProperty().bind(
                Bindings.format("电脑 生命值：%d / %d", desktopContext.getAiPlayer().getHp(), desktopContext.getAiPlayer().getMaxHp())
        );
        Label aiPlayerDeadCountLabel = new Label();
        aiPlayerDeadCountLabel.setFont(StyleConstants.font24);
        aiPlayerDeadCountLabel.textProperty().bind(
                Bindings.format("%s 伤亡数：%d / %d", desktopContext.getAiPlayer().getFormation().getName(), desktopContext.getAiPlayer().getDeadCount(), desktopContext.getAiPlayer().getMaxDeadCount())
        );
        VBox aiPlayerInfo = new VBox();
        aiPlayerInfo.getChildren().addAll(aiPlayerHpLabel, aiPlayerDeadCountLabel);

        gameBoardPaneTop.setLeft(peoplePlayerInfo);
        gameBoardPaneTop.setCenter(turnCountLabel);
        gameBoardPaneTop.setRight(aiPlayerInfo);

        BorderPane layoutPane = new BorderPane();
        layoutPane.setLeft(desktopContext.getShowIndexSceneBtn());
        Label helpTextLabel = new Label("兵种克制关系：枪->骑->甲->术->盾->射->枪，器械无克制关系");
        helpTextLabel.setFont(StyleConstants.font24);
        layoutPane.setCenter(helpTextLabel);
        gameBoardPaneTop.setTop(layoutPane);

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
                    if (desktopContext.getPeoplePlayer().getCurCard() != null) {
                        boolean putFlag = false;
                        ToggleButton selectCard = desktopContext.getPeoplePlayer().getCurCard();
                        Object userData = selectCard.getUserData();
                        if (userData instanceof UnitCard unitCard) {
                            if (finalCol == DesktopContext.peoplePlayerUnitInitColIndex && !desktopContext.getPeoplePlayer().isCurTurnPutUnitCardFlag()) {
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
                            if (finalCol == DesktopContext.peoplePlayerEqColIndex && !desktopContext.getPeoplePlayer().isCurTurnPutEqCardFlag()) {
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
                            if (finalCol == DesktopContext.peoplePlayerUnitInitColIndex && !desktopContext.getPeoplePlayer().isCurTurnPutTacticCardFlag()) {
                                BorderPane execTacticRoot = UIComponentFactory.createExecTacticRoot(tacticCard, finalRow, desktopContext.getPeoplePlayer());
                                desktopContext.getPlayGameSceneRoot().getChildren().addAll(desktopContext.getScrim(), execTacticRoot);
                                putFlag = true;
                                desktopContext.getPeoplePlayer().setCurTurnPutTacticCardFlag(true);
                            }
                        }

                        if (putFlag) {
                            desktopContext.getPeoplePlayer().setCurCard(null);
                            desktopContext.getCardList().remove(selectCard);
                            desktopContext.getSelectCardCenter().getChildren().remove(selectCard);
                        }
                    }
                });
                gridPane.add(cell, col, row);
                cells[row][col] = cell;
            }
        }

        HBox gameBoardPaneBottom = new HBox(10);
        gameBoardPaneBottom.setAlignment(Pos.CENTER);
        gameBoardPaneBottom.setPadding(new Insets(10));
        Button beginTurnBtn = new Button("开始回合");
        beginTurnBtn.setFont(StyleConstants.font20);
        Button selectCardBtn = new Button("选择卡牌");
        selectCardBtn.setFont(StyleConstants.font20);
        selectCardBtn.setDisable(true);
        Button endTurnBtn = new Button("结束操作");
        endTurnBtn.setFont(StyleConstants.font20);
        endTurnBtn.setDisable(true);
        gameBoardPaneBottom.getChildren().addAll(beginTurnBtn, selectCardBtn, endTurnBtn);
        desktopContext.setBeginTurnBtn(beginTurnBtn);
        desktopContext.setSelectCardBtn(selectCardBtn);
        desktopContext.setEndTurnBtn(endTurnBtn);

        beginTurnBtn.setOnAction(new BeginTurnEvent());
        endTurnBtn.setOnAction(new EndTurnEvent());

        BorderPane gameBoardPane = new BorderPane();
        gameBoardPane.setBackground(StyleConstants.BLUE_BACKGROUND);
        gameBoardPane.setTop(gameBoardPaneTop);
        gameBoardPane.setCenter(gridPane);
        gameBoardPane.setBottom(gameBoardPaneBottom);

        // 选择卡牌
        BorderPane selectCardRoot = UIComponentFactory.createSelectCardRoot(desktopContext);

        // 跳转
        selectCardBtn.setOnAction(e -> {
            desktopContext.getPeoplePlayer().setCurCard(null);
            for (ToggleButton toggleButton : desktopContext.getCardList()) {
                toggleButton.setSelected(false);
            }
            playGameRoot.getChildren().addAll(desktopContext.getScrim(), selectCardRoot);
        });

        playGameRoot.getChildren().addAll(gameBoardPane);
        return playGameRoot;
    }

}
