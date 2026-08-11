package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.TacticCard;
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

}
