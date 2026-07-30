package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.data.HeroData;
import drintau.game.sanguokapai.data.UnitDataFactory;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;

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

    private StackPane[][] cells;

    private int nowRowIndex = 0;
    private int nowColIndex = 0;

    private int preRowIndex = 0;
    private int preColIndex = 0;

    private ArrayDeque<ActionItem> actionDeque = new ArrayDeque<>();
    private ArrayDeque<ActionItem> nextActionDeque = new ArrayDeque<>();

    public void init() {
        UnitDataFactory unitDataFactory = new UnitDataFactory();
        actionDeque.add(new ActionItem(true, 0,0, HeroData.GUAN_YU));
        actionDeque.add(new ActionItem(false, 0,13, unitDataFactory.createQiangBing()));
    }

}
