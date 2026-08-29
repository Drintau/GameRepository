package drintau.game.sanguokapai.desktop.scene;

import drintau.game.sanguokapai.card.EquipmentCard;
import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.*;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.util.List;

public class ShowAllCardScene extends Scene {

    private static Button heroPaginationBtn = new Button("英雄卡");
    private static Button soldierPaginationBtn = new Button("士兵卡");
    private static Button equipmentPaginationBtn = new Button("装备卡");
    private static Button tacticPaginationBtn = new Button("策略卡");

    public ShowAllCardScene() {
        super(createContent());
    }

    private static Parent createContent() {
        BorderPane showAllCardRoot = new BorderPane();
        showAllCardRoot.setBackground(StyleConstants.BLUE_BACKGROUND);
        showAllCardRoot.setPadding(new Insets(10));

        Pagination heroPagination = heroPagination();
        Pagination soldierPagination = soldierPagination();
        Pagination equipmentPagination = equipmentPagination();
        Pagination tacticPagination = tacticPagination();

        heroPaginationBtn.setFont(StyleConstants.font20);
        heroPaginationBtn.setOnAction(event -> {
            changeShow(heroPaginationBtn);
            showAllCardRoot.setCenter(heroPagination);
        });

        soldierPaginationBtn.setFont(StyleConstants.font20);
        soldierPaginationBtn.setOnAction(event -> {
            changeShow(soldierPaginationBtn);
            showAllCardRoot.setCenter(soldierPagination);
        });

        equipmentPaginationBtn.setFont(StyleConstants.font20);
        equipmentPaginationBtn.setOnAction(event -> {
            changeShow(equipmentPaginationBtn);
            showAllCardRoot.setCenter(equipmentPagination);
        });

        tacticPaginationBtn.setFont(StyleConstants.font20);
        tacticPaginationBtn.setOnAction(event -> {
            changeShow(tacticPaginationBtn);
            showAllCardRoot.setCenter(tacticPagination);
        });

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(DesktopContext.getInstance().getShowIndexSceneBtn(), heroPaginationBtn, soldierPaginationBtn, equipmentPaginationBtn, tacticPaginationBtn);
        showAllCardRoot.setTop(hBox);

        return showAllCardRoot;
    }

    private static Pagination heroPagination() {
        List<UnitCard> allHeroes = HeroData.getAllHeroes();
        Pagination pagination = new Pagination(allHeroes.size(), 0);
        pagination.setPageFactory(pageIndex -> {
            Label label = new Label(allHeroes.get(pageIndex).getDescription());
            label.setFont(StyleConstants.font24);
            return new StackPane(label);
        });
        return pagination;
    }

    private static Pagination soldierPagination() {
        List<UnitCard> allSoldiers = SoldierData.getAllSoldiers();
        Pagination pagination = new Pagination(allSoldiers.size(), 0);
        pagination.setPageFactory(pageIndex -> {
            Label label = new Label(allSoldiers.get(pageIndex).getDescription());
            label.setFont(StyleConstants.font24);
            return new StackPane(label);
        });
        return pagination;
    }

    private static Pagination equipmentPagination() {
        List<EquipmentCard> allEquipments = EquipmentData.getAllEquipments();
        Pagination pagination = new Pagination(allEquipments.size(), 0);
        pagination.setPageFactory(pageIndex -> {
            Label label = new Label(allEquipments.get(pageIndex).getDescription());
            label.setFont(StyleConstants.font24);
            return new StackPane(label);
        });
        return pagination;
    }

    private static Pagination tacticPagination() {
        List<TacticCard> allTactics = TacticData.getAllTactics();
        Pagination pagination = new Pagination(allTactics.size(), 0);
        pagination.setPageFactory(pageIndex -> {
            Label label = new Label(allTactics.get(pageIndex).getDescription());
            label.setFont(StyleConstants.font24);
            return new StackPane(label);
        });
        return pagination;
    }

    private static void changeShow(Button btn) {
        heroPaginationBtn.setDisable(false);
        soldierPaginationBtn.setDisable(false);
        equipmentPaginationBtn.setDisable(false);
        tacticPaginationBtn.setDisable(false);

        btn.setDisable(true);
    }

}
