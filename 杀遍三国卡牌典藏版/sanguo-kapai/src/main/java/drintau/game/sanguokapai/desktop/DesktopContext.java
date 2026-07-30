package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.data.HeroData;
import drintau.game.sanguokapai.data.UnitDataFactory;
import drintau.game.sanguokapai.util.DaemonScheduler;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;

@Setter
@Getter
public class DesktopContext {

    private DesktopContext(){}
    private static class InitDesktopContext {
        private static final DesktopContext INSTANCE = new DesktopContext();
    }
    public static DesktopContext getInstance(){
        return InitDesktopContext.INSTANCE;
    }

    public static final int rows = 3; // 行
    public static final int cols = 14; // 列
    public static final int player1EqColIndex = 0; // 玩家1装备列
    public static final int player2EqColIndex = 13; // 玩家2装备列
    public static final int player1UnitInitColIndex = 1; // 玩家1出兵列
    public static final int player2UnitInitColIndex = 12; // 玩家2出兵列
    public static final int moveMinColIndex = 2; // 单位移动最小列
    public static final int moveMaxColIndex = 11; // 单位移动最大列

    private StackPane[][] cells;

    private int nowRowIndex = 0;
    private int nowColIndex = 0;

    private int preRowIndex = 0;
    private int preColIndex = 0;

    private ArrayDeque<ActionItem> actionDeque = new ArrayDeque<>();
    private ArrayDeque<ActionItem> nextActionDeque = new ArrayDeque<>();

    public void init() {
        UnitDataFactory unitDataFactory = new UnitDataFactory();
        actionDeque.add(new ActionItem(true, 0,player1UnitInitColIndex, HeroData.GUAN_YU));
        actionDeque.add(new ActionItem(false, 0,player2UnitInitColIndex, unitDataFactory.createQiangBing()));

        nextActionDeque.addAll(actionDeque);
        // 出兵
        DaemonScheduler.getInstance().submitOnceDelayTask(() -> {

            ActionItem actionItem;
            while ((actionItem = nextActionDeque.pollFirst()) != null) {
                if (actionItem.getCurColIndex() == DesktopContext.player1UnitInitColIndex || actionItem.getCurColIndex() == DesktopContext.player2UnitInitColIndex) {
                    int initRowIndex = actionItem.getCurRowIndex();
                    int initColIndex = actionItem.getCurColIndex();
                    ActionItem cellData = actionItem;
                    Platform.runLater(() -> {
                        cells[initRowIndex][initColIndex].getChildren().addAll(new Label(cellData.getUnitCard().getName()));
                        cells[initRowIndex][initColIndex].setUserData(cellData);
                    });
                }
            }
        }, 1L, TimeUnit.SECONDS);
    }

}
