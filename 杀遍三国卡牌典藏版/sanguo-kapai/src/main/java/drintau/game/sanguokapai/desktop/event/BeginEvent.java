package drintau.game.sanguokapai.desktop.event;

import drintau.game.sanguokapai.data.HeroData;
import drintau.game.sanguokapai.desktop.ActionItem;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.util.DaemonScheduler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

public class BeginEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        DesktopContext desktopContext = DesktopContext.getInstance();

        DaemonScheduler.getInstance().submitOnceDelayTask(() -> {
            StackPane[][] cells = desktopContext.getCells();

            ArrayDeque<ActionItem> actionDeque = desktopContext.getActionDeque();
            ActionItem actionItem;
            while ((actionItem = actionDeque.pollFirst()) != null) {
                String unitName = actionItem.getUnitCard().getName();
                if (cells[actionItem.getCurRowIndex()][actionItem.getCurColIndex()].getChildren().isEmpty()) {
                    int initRowIndex = actionItem.getCurRowIndex();
                    int initColIndex = actionItem.getCurColIndex();
                    Platform.runLater(() -> {
                        cells[initRowIndex][initColIndex].getChildren().addAll(new Label(unitName));
                    });
                    try {
                        Thread.sleep(1000L);  // 参数是毫秒，1000毫秒 = 1秒
                    } catch (InterruptedException e) {
                        // 线程被中断时的处理（通常恢复中断标志或退出）
                        Thread.currentThread().interrupt();
                    }
                }
                for (int i = actionItem.getUnitCard().getSpeed(); i > 0 ; i--) {
                    int preColIndex = actionItem.getCurColIndex();
                    if (actionItem.isPlayer1()) {
                        actionItem.setCurColIndex(actionItem.getCurColIndex() + 1);
                    } else {
                        actionItem.setCurColIndex(actionItem.getCurColIndex() - 1);
                    }
                    int curRowIndex = actionItem.getCurRowIndex();
                    int curColIndex = actionItem.getCurColIndex();
                    Platform.runLater(() -> {
                        cells[curRowIndex][preColIndex].getChildren().clear();
                        cells[curRowIndex][curColIndex].getChildren().addAll(new Label(unitName));
                    });

                    try {
                        Thread.sleep(1000L);  // 参数是毫秒，1000毫秒 = 1秒
                    } catch (InterruptedException e) {
                        // 线程被中断时的处理（通常恢复中断标志或退出）
                        Thread.currentThread().interrupt();
                    }
                }
                desktopContext.getNextActionDeque().add(actionItem);
            }
            desktopContext.getActionDeque().addAll(desktopContext.getNextActionDeque());
            desktopContext.getNextActionDeque().clear();
        }, 1L, TimeUnit.SECONDS);

    }

}
