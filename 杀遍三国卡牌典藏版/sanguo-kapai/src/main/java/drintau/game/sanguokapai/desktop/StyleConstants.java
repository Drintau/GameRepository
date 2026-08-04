package drintau.game.sanguokapai.desktop;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class StyleConstants {

    // 界面背景：蓝色透明度
    public static final Background BLUE_BACKGROUND = Background.fill(Color.web("#2196F3", 0.1));

    // 棋盘格子背景：浅灰色透明度
    public static final Background LIGHTGRAY_BACKGROUND = Background.fill(Color.web("#D3D3D3", 0.5));

    // 棋盘格子默认边框：绿色透明度
    public static final Border CELL_BORDER_DEFAULT = new Border(new BorderStroke(Color.web("#00FF00", 0.3), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));
    // 棋盘格子行动时边框：电脑计策卡、电脑装备区，紫色
    public static final Border CELL_BORDER_ACTION = new Border(new BorderStroke(Color.web("#FF00FF", 1.0), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    // 玩家单位、本阵：黄色透明
    public static final Background PLAYER_UNIT_BACKGROUND = Background.fill(Color.web("#FFFF00", 0.3));

    // 电脑单位、本阵：红色透明
    public static final Background RED_BACKGROUND = Background.fill(Color.web("#FF0000", 0.3));

    // 计策：青色透明
    public static final Background PLAYER_TACTIC_BACKGROUND = Background.fill(Color.web("#00FFFF", 0.3));

    // 装备、装备区、单纯展示文本用的背景：白色
    public static final Background WHITE_BACKGROUND = Background.fill(Color.web("#FFFFFF", 1.0));

    public static final Font font16 = Font.font(16); // 卡牌文字用
    public static final Font font20 = Font.font(20); // 按钮用
    public static final Font font24 = Font.font(24); // 标题、展示值用
}
