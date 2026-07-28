package drintau.game.sanguokapai.desktop;

import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

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

}
