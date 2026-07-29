package drintau.game.sanguokapai.desktop.event;

import drintau.game.sanguokapai.data.HeroData;
import drintau.game.sanguokapai.desktop.DesktopContext;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class BeginEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        int nextRow = desktopContext.getNowRowIndex() + 1;
        int nextCol = desktopContext.getNowColIndex() + 1;

        StackPane[][] cells = desktopContext.getCells();

        if (!(desktopContext.getNowRowIndex() == desktopContext.getPreRowIndex() && desktopContext.getNowColIndex() == desktopContext.getPreColIndex())) {
            cells[desktopContext.getPreRowIndex()][desktopContext.getPreColIndex()].getChildren().clear();
        }

        cells[desktopContext.getNowRowIndex()][desktopContext.getNowColIndex()].getChildren().addAll(new Label(HeroData.GUAN_YU.getName()));
        desktopContext.setPreRowIndex(desktopContext.getNowRowIndex());
        desktopContext.setPreColIndex(desktopContext.getNowColIndex());

        if (nextCol >= DesktopContext.cols) {
            desktopContext.setNowColIndex(0);
            if (nextRow >= DesktopContext.rows) {
                desktopContext.setNowRowIndex(0);
            } else {
                desktopContext.setNowRowIndex(nextRow);
            }
        } else {
            desktopContext.setNowColIndex(nextCol);
        }

    }

}
