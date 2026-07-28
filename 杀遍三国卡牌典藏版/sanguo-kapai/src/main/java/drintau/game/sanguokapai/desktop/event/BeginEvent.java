package drintau.game.sanguokapai.desktop.event;

import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;

public class BeginEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        int nextRow = desktopContext.getNowRowIndex() + 1;
        int nextCol = desktopContext.getNowColIndex() + 1;

        StackPane[][] cells = desktopContext.getCells();

        if (!(desktopContext.getNowRowIndex() == desktopContext.getPreRowIndex() && desktopContext.getNowColIndex() == desktopContext.getPreColIndex())) {
            cells[desktopContext.getPreRowIndex()][desktopContext.getPreColIndex()].setBackground(StyleConstants.LIGHTGRAY_BACKGROUND);
        }

        cells[desktopContext.getNowRowIndex()][desktopContext.getNowColIndex()].setBackground(StyleConstants.BLUE_BACKGROUND);
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
