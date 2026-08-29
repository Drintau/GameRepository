package drintau.game.sanguokapai.desktop.scene;

import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.FormationFactory;
import drintau.game.sanguokapai.data.HeroData;
import drintau.game.sanguokapai.data.formation.AbstractFormation;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.List;

public class ShowAllCardScene extends Scene {

    public ShowAllCardScene() {
        super(createContent());
    }

    private static Parent createContent() {
        BorderPane showAllCardRoot = new BorderPane();
        showAllCardRoot.setBackground(StyleConstants.BLUE_BACKGROUND);
        showAllCardRoot.setPadding(new Insets(10));

        showAllCardRoot.setTop(DesktopContext.getInstance().getShowIndexSceneBtn());

        Pagination heroPagination = heroPagination();

        showAllCardRoot.setCenter(heroPagination);

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

}
