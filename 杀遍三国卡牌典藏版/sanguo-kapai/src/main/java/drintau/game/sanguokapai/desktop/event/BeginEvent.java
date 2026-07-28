package drintau.game.sanguokapai.desktop.event;

import drintau.game.sanguokapai.desktop.DesktopContext;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class BeginEvent implements EventHandler<ActionEvent> {

    private final Background blueBackground = Background.fill(Color.web("#2196F3", 1.0));
    private final Background whiteBackground = Background.fill(Color.web("#FFFFFF", 1.0));

    @Override
    public void handle(ActionEvent event) {
        DesktopContext desktopContext = DesktopContext.getInstance();
        int nextRow = desktopContext.getNowRowIndex() + 1;
        int nextCol = desktopContext.getNowColIndex() + 1;

        StackPane[][] cells = desktopContext.getCells();

        if (!(desktopContext.getNowRowIndex() == desktopContext.getPreRowIndex() && desktopContext.getNowColIndex() == desktopContext.getPreColIndex())) {
            cells[desktopContext.getPreRowIndex()][desktopContext.getPreColIndex()].setBackground(whiteBackground);
        }

        cells[desktopContext.getNowRowIndex()][desktopContext.getNowColIndex()].setBackground(blueBackground);
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
