package drintau.game.sanguokapai.desktop.scene;

import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HelpScene extends Scene {

    public HelpScene(Stage stage) {
        super(createContent(stage));
    }

    private static Parent createContent(Stage stage) {
        DesktopContext desktopContext = DesktopContext.getInstance();

        BorderPane helpRoot = new BorderPane();
        helpRoot.setBackground(StyleConstants.BLUE_BACKGROUND);
        helpRoot.setPadding(new Insets(10));

        helpRoot.setTop(desktopContext.getShowIndexSceneBtn());

        Label helpTextLabel = new Label("""
                1、卡牌包括：策略卡、装备卡、单位卡
                2、每回合各种卡牌最多放置一张
                3、装备卡点击装备区放置，单位卡点击本阵区放置，策略卡点击本阵区使用
                4、阵型包含的兵种和单位数量不同，具备一定程度的克制关系
                5、兵种克制关系：枪->骑->甲->术->盾->射->枪，器械无克制关系
                6、让对手生命值归0或者让对手伤亡单位数达到指定值，都可以获胜
                7、生命值固定为100，伤亡单位数上限由阵型单位数决定
                8、场上没有敌方单位时，会触发“冲锋模式”，己方单位无视移动力前进
                9、电脑加强：每5回合，抽到策略卡概率上升；单位伤亡数上限较高
                """);
        helpTextLabel.setFont(StyleConstants.font24);
        helpRoot.setCenter(helpTextLabel);

        return helpRoot;
    }

}
