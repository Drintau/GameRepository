package drintau.game.sanguokapai.desktop;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class StyleConstants {

    public static final Background BLUE_BACKGROUND = Background.fill(Color.web("#2196F3", 0.1));
    public static final Background LIGHTGRAY_BACKGROUND = Background.fill(Color.web("#D3D3D3", 0.5));
    public static final Background WHITE_BACKGROUND = Background.fill(Color.web("#FFFFFF", 1.0));

    public static final Border CELL_BORDER = new Border(new BorderStroke(Color.BEIGE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));

    public static final Font font16 = Font.font(16); // 卡牌文字用
    public static final Font font20 = Font.font(20); // 按钮用
    public static final Font font24 = Font.font(24); // 标题、展示值用
}
