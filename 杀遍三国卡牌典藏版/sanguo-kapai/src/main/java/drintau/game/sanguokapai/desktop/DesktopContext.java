package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.HeroData;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.data.SoldierData;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

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

    private IntegerProperty turnCount = new SimpleIntegerProperty(1);

    private StackPane[][] cells;

    // 玩游戏界面的控件
    private Button beginTurn;
    private Button selectCard;
    private Button endTurn;
    // 选卡牌界面的控件
    private HBox cardSelectCenter;
    private List<ToggleButton> cardList = new ArrayList<>(5);
    // 战斗界面需要的控件
    private StackPane root;
    private BorderPane attackRoot;
    private final Object battleLock = new Object();

    private ArrayDeque<ActionItem> actionDeque = new ArrayDeque<>();
    private ArrayDeque<ActionItem> nextActionDeque = new ArrayDeque<>();

    private List<UnitCard> heroList = HeroData.getAllHeroes();
    private List<UnitCard> soldierList = SoldierData.getAllSoldiers();

    private ArrayDeque<PlayerData> playerDeque = new ArrayDeque<>();
    private ArrayDeque<PlayerData> nextPlayerDeque = new ArrayDeque<>();
    private PlayerData aiPlayer = new PlayerData();
    private PlayerData peoplePlayer = new PlayerData();
    private Label player1HpLabel;
    private Label player2HpLabel;

    public void playerInit() {
        aiPlayer.setAiFlag(true);
        aiPlayer.setEqColIndex(player2EqColIndex);
        aiPlayer.setUnitInitColIndex(player2UnitInitColIndex);
        player2HpLabel.textProperty().bind(
                Bindings.format("电脑 生命值：%d / %d", aiPlayer.getHp(), aiPlayer.getMaxHp())
        );

        peoplePlayer.setAiFlag(false);
        peoplePlayer.setEqColIndex(player1EqColIndex);
        peoplePlayer.setUnitInitColIndex(player1UnitInitColIndex);
        player1HpLabel.textProperty().bind(
                Bindings.format("玩家 生命值：%d / %d", peoplePlayer.getHp(), peoplePlayer.getMaxHp())
        );

        playerDeque.add(aiPlayer);
        playerDeque.add(peoplePlayer);
    }

}
