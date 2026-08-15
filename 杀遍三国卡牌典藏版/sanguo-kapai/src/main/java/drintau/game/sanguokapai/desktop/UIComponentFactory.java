package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.EquipmentCard;
import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.PlayerData;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class UIComponentFactory {

    public static BorderPane createExecTacticRoot(TacticCard tacticCard, int rowIndex, PlayerData playerData) {
        BorderPane execTacticRoot = new BorderPane();
        execTacticRoot.setPadding(new Insets(10));
        execTacticRoot.setBackground(StyleConstants.WHITE_BACKGROUND);
        execTacticRoot.setPrefWidth(300);
        execTacticRoot.setPrefHeight(220);
        execTacticRoot.setMinSize(300, 220);
        execTacticRoot.setMaxSize(300, 220);

        Label execTacticTitle;
        if (playerData.isAiFlag()) {
            execTacticTitle = new Label("电脑执行计策");
        } else {
            execTacticTitle = new Label("玩家执行计策");
        }
        execTacticTitle.setFont(StyleConstants.font24);
        execTacticRoot.setTop(execTacticTitle);
        BorderPane.setAlignment(execTacticTitle, Pos.CENTER);

        Label execTacticCenter = new Label(tacticCard.getDescription());
        execTacticCenter.setWrapText(true);
        execTacticCenter.setFont(StyleConstants.font16);
        execTacticRoot.setCenter(execTacticCenter);

        HBox execTacticBottom = new HBox(10);
        execTacticBottom.setAlignment(Pos.CENTER);
        if (playerData.isAiFlag()) {
            Label aiExecTacticLabel = new Label("请勿操作！电脑行动中");
            aiExecTacticLabel.setFont(StyleConstants.font20);
            execTacticBottom.getChildren().addAll(aiExecTacticLabel);
            execTacticRoot.setBottom(execTacticBottom);
        } else {
            Button execTacticSureButton = new Button("确认");
            execTacticSureButton.setFont(StyleConstants.font20);
            execTacticSureButton.setOnAction(e -> {
                tacticCard.exec(playerData, rowIndex);
                DesktopContext.getInstance().getRoot().getChildren().removeAll(DesktopContext.getInstance().getScrim(), execTacticRoot);
            });
            execTacticBottom.getChildren().addAll(execTacticSureButton);
        }
        execTacticRoot.setBottom(execTacticBottom);

        return execTacticRoot;
    }

    public static BorderPane createAttackRoot(DesktopContext desktopContext) {
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
            desktopContext.getRoot().getChildren().removeAll(desktopContext.getScrim(), desktopContext.getAttackRoot());
            synchronized (desktopContext.getBattleLock()) {
                desktopContext.getBattleLock().notify();
            }
        });
        attackBottom.getChildren().addAll(attackSureButton);
        attackRoot.setBottom(attackBottom);

        return attackRoot;
    }

    public static BorderPane createSelectCardRoot(DesktopContext desktopContext) {
        BorderPane selectCardRoot = new BorderPane();
        selectCardRoot.setPadding(new Insets(10));
        selectCardRoot.setBackground(StyleConstants.WHITE_BACKGROUND);
        selectCardRoot.setPrefWidth(600);
        selectCardRoot.setPrefHeight(300);
        selectCardRoot.setMinSize(600, 300);
        selectCardRoot.setMaxSize(600, 300);

        Label selectCardTitle = new Label("选择卡牌");
        selectCardTitle.setFont(StyleConstants.font24);
        selectCardRoot.setTop(selectCardTitle);
        BorderPane.setAlignment(selectCardTitle, Pos.CENTER);

        HBox selectCardCenter = new HBox(10);
        selectCardCenter.setAlignment(Pos.CENTER);
        selectCardRoot.setCenter(selectCardCenter);

        // 这个很重要
        desktopContext.setSelectCardCenter(selectCardCenter);

        HBox selectCardBottom = new HBox(10);
        selectCardBottom.setAlignment(Pos.CENTER);
        Button selectCardSureButton = new Button("确认");
        selectCardSureButton.setFont(StyleConstants.font20);
        Button selectCardCloseButton = new Button("关闭");
        selectCardCloseButton.setFont(StyleConstants.font20);
        selectCardBottom.getChildren().addAll(selectCardSureButton, selectCardCloseButton);
        selectCardRoot.setBottom(selectCardBottom);

        selectCardSureButton.setOnAction(e -> {
            if (desktopContext.getPeoplePlayer().getSelectCard() != null) {
                Object userData = desktopContext.getPeoplePlayer().getSelectCard().getUserData();
                if (userData instanceof UnitCard) {
                    if (!desktopContext.getPeoplePlayer().isCurTurnPutUnitCardFlag()) {
                        desktopContext.getRoot().getChildren().removeAll(desktopContext.getScrim(), selectCardRoot);
                    }
                } else if (userData instanceof EquipmentCard) {
                    if (!desktopContext.getPeoplePlayer().isCurTurnPutEqCardFlag()) {
                        desktopContext.getRoot().getChildren().removeAll(desktopContext.getScrim(), selectCardRoot);
                    }
                } else if (userData instanceof TacticCard) {
                    if (!desktopContext.getPeoplePlayer().isCurTurnPutTacticCardFlag()) {
                        desktopContext.getRoot().getChildren().removeAll(desktopContext.getScrim(), selectCardRoot);
                    }
                }
            }
        });
        selectCardCloseButton.setOnAction(e -> {
            desktopContext.getPeoplePlayer().setSelectCard(null);
            desktopContext.getRoot().getChildren().removeAll(desktopContext.getScrim(), selectCardRoot);
        });

        return selectCardRoot;
    }

}
