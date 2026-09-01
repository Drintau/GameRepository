package drintau.game.sanguokapai.desktop.scene;

import drintau.game.sanguokapai.data.FormationFactory;
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

public class ShowAllFormationScene extends Scene {

    public ShowAllFormationScene() {
        super(createContent());
    }

    private static Parent createContent() {
        BorderPane showAllFormationRoot = new BorderPane();
        showAllFormationRoot.setBackground(StyleConstants.BLUE_BACKGROUND);
        showAllFormationRoot.setPadding(new Insets(10));

        showAllFormationRoot.setTop(DesktopContext.getInstance().getShowIndexSceneBtn());

        List<String> formationNameList = FormationFactory.FORMATION_NAME_LIST;

        Pagination pagination = new Pagination(formationNameList.size(), 0);
        pagination.setPageFactory(pageIndex -> {
            AbstractFormation formation = FormationFactory.getFormation(formationNameList.get(pageIndex));

            Label formationInfoLabel = new Label(formation.getDescription());
            formationInfoLabel.setFont(StyleConstants.font24);

            return new StackPane(formationInfoLabel);
        });

        showAllFormationRoot.setCenter(pagination);

        return showAllFormationRoot;
    }

}
