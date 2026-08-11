package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.EquipmentCard;
import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.EnumMap;
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
    public static final int peoplePlayerEqColIndex = 0; // 玩家装备列
    public static final int aiPlayerEqColIndex = 13; // 电脑装备列
    public static final int peoplePlayerUnitInitColIndex = 1; // 玩家出兵列
    public static final int aiPlayerUnitInitColIndex = 12; // 电脑出兵列
    public static final int moveMinColIndex = 2; // 单位移动最小列
    public static final int moveMaxColIndex = 11; // 单位移动最大列
    public static final int maxSpeed = 10; // 给冲锋模式用的

    // 回合计数
    private IntegerProperty turnCount = new SimpleIntegerProperty(1);

    // 根节点
    private StackPane root;
    // 遮盖层
    private Rectangle scrim;
    // 棋盘格子
    private StackPane[][] cells;
    // 回合控件
    private Button beginTurn;
    private Button selectCard;
    private Button endTurn;
    // 选卡牌控件
    private HBox cardSelectCenter;
    private List<ToggleButton> cardList = new ArrayList<>(5);
    // 战斗控件
    private BorderPane attackRoot;
    // 战斗锁
    private final Object battleLock = new Object();
    // 电脑行动锁
    private final Object aiActionLock = new Object();

    // 行动单位队列
    private ArrayDeque<ActionItem> actionDeque = new ArrayDeque<>();
    private ArrayDeque<ActionItem> nextActionDeque = new ArrayDeque<>();

    // 卡牌
    private List<UnitCard> heroList = HeroData.getAllHeroes();
    private List<UnitCard> soldierList = SoldierData.getAllSoldiers();
    private List<EquipmentCard> equipmentList = EquipmentData.getAllEquipments();
    private List<TacticCard> tacticList = TacticData.getAllTactics();
    // 兵种克制关系
    public EnumMap<CardConstants.UnitType, CardConstants.UnitType> ADVANTAGE_MAP = new EnumMap<>(CardConstants.UnitType.class);

    // 玩家行动
    private ArrayDeque<PlayerData> playerDeque = new ArrayDeque<>();
    private ArrayDeque<PlayerData> nextPlayerDeque = new ArrayDeque<>();
    private PlayerData aiPlayer = new PlayerData();
    private PlayerData peoplePlayer = new PlayerData();

    // 游戏结束标记
    private boolean gameOverFlag;

    // 初始化
    public void init(StackPane root) {
        ADVANTAGE_MAP.put(CardConstants.UnitType.GUNNER, CardConstants.UnitType.CAVALRY);   // 枪 → 骑
        ADVANTAGE_MAP.put(CardConstants.UnitType.CAVALRY, CardConstants.UnitType.ARMOR);    // 骑 → 甲
        ADVANTAGE_MAP.put(CardConstants.UnitType.ARMOR, CardConstants.UnitType.MAGE);       // 甲 → 术
        ADVANTAGE_MAP.put(CardConstants.UnitType.MAGE, CardConstants.UnitType.SHIELD);      // 术 → 盾
        ADVANTAGE_MAP.put(CardConstants.UnitType.SHIELD, CardConstants.UnitType.SHOOTER);   // 盾 → 射
        ADVANTAGE_MAP.put(CardConstants.UnitType.SHOOTER, CardConstants.UnitType.GUNNER);   // 射 → 枪
        ADVANTAGE_MAP.put(CardConstants.UnitType.SIEGE, CardConstants.UnitType.NONE);   // 器械不克制任何，也不被任何克制

        this.root = root;
        scrim = new Rectangle();
        scrim.widthProperty().bind(root.widthProperty());
        scrim.heightProperty().bind(root.heightProperty());
        scrim.setFill(Color.color(0, 0.5, 0, 0.2));

        aiPlayer.setAiFlag(true);
        aiPlayer.setEqColIndex(aiPlayerEqColIndex);
        aiPlayer.setUnitInitColIndex(aiPlayerUnitInitColIndex);

        peoplePlayer.setAiFlag(false);
        peoplePlayer.setEqColIndex(peoplePlayerEqColIndex);
        peoplePlayer.setUnitInitColIndex(peoplePlayerUnitInitColIndex);

        playerDeque.add(aiPlayer);
        playerDeque.add(peoplePlayer);
    }

    public void testGameOver() {
        if (this.getPeoplePlayer().getHp().get() <= 0) {
            if (!this.isGameOverFlag()) {
                BorderPane gameOverPane = new BorderPane();
                Label gameOverLabel = new Label("游戏结束！很遗憾输了！请关闭程序重新游玩。");
                gameOverLabel.setBackground(StyleConstants.WHITE_BACKGROUND);
                gameOverLabel.setFont(StyleConstants.font24);
                gameOverPane.setCenter(gameOverLabel);
                this.setGameOverFlag(true);
                Platform.runLater(() -> {
                    this.getRoot().getChildren().addAll(this.getScrim(), gameOverPane);
                });
            }
        } else if (this.getAiPlayer().getHp().get() <= 0) {
            if (!this.isGameOverFlag()) {
                BorderPane gameOverPane = new BorderPane();
                Label gameOverLabel = new Label("游戏结束！恭喜赢了！请关闭程序重新游玩。");
                gameOverLabel.setBackground(StyleConstants.WHITE_BACKGROUND);
                gameOverLabel.setFont(StyleConstants.font24);
                gameOverPane.setCenter(gameOverLabel);
                this.setGameOverFlag(true);
                Platform.runLater(() -> {
                    this.getRoot().getChildren().addAll(this.getScrim(), gameOverPane);
                });
            }
        }
    }

}
