package drintau.game.sanguokapai.desktop;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.EquipmentCard;
import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.*;
import drintau.game.sanguokapai.desktop.scene.IndexScene;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

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
    private IntegerProperty turnCount;

    // 场景跳转
    private Stage stage;
    private IndexScene indexScene;
    private Button showIndexSceneBtn;
    // 玩游戏场景的根节点
    private StackPane playGameSceneRoot;
    // 遮盖层
    private Rectangle scrim;
    // 棋盘格子
    private StackPane[][] cells;
    // 回合控件
    private Button beginTurnBtn;
    private Button selectCardBtn;
    private Button endTurnBtn;
    // 选卡牌控件
    private HBox selectCardCenter;
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
    private PlayerData aiPlayer;
    private PlayerData peoplePlayer;

    // 游戏结束标记
    private boolean gameOverFlag;

    // 初始化
    public void init(StackPane root) {
        turnCount = new SimpleIntegerProperty(1);
        playGameSceneRoot = null;
        scrim = null;
        cells = null;
        beginTurnBtn = null;
        selectCardBtn = null;
        endTurnBtn = null;
        selectCardCenter = null;
        cardList.clear();
        attackRoot = null;
        actionDeque.clear();
        nextActionDeque.clear();
        ADVANTAGE_MAP.clear();
        playerDeque.clear();
        nextPlayerDeque.clear();
        aiPlayer = new PlayerData();
        peoplePlayer = new PlayerData();
        gameOverFlag = false;

        ADVANTAGE_MAP.put(CardConstants.UnitType.GUNNER, CardConstants.UnitType.CAVALRY);   // 枪 → 骑
        ADVANTAGE_MAP.put(CardConstants.UnitType.CAVALRY, CardConstants.UnitType.ARMOR);    // 骑 → 甲
        ADVANTAGE_MAP.put(CardConstants.UnitType.ARMOR, CardConstants.UnitType.MAGE);       // 甲 → 术
        ADVANTAGE_MAP.put(CardConstants.UnitType.MAGE, CardConstants.UnitType.SHIELD);      // 术 → 盾
        ADVANTAGE_MAP.put(CardConstants.UnitType.SHIELD, CardConstants.UnitType.SHOOTER);   // 盾 → 射
        ADVANTAGE_MAP.put(CardConstants.UnitType.SHOOTER, CardConstants.UnitType.GUNNER);   // 射 → 枪
        ADVANTAGE_MAP.put(CardConstants.UnitType.SIEGE, CardConstants.UnitType.NONE);   // 器械不克制任何，也不被任何克制

        this.playGameSceneRoot = root;
        scrim = new Rectangle();
        scrim.widthProperty().bind(root.widthProperty());
        scrim.heightProperty().bind(root.heightProperty());
        scrim.setFill(Color.color(0, 0.5, 0, 0.2));

        this.attackRoot = UIComponentFactory.createAttackRoot(this);

        aiPlayer.setAiFlag(true);
        aiPlayer.setEqColIndex(aiPlayerEqColIndex);
        aiPlayer.setUnitInitColIndex(aiPlayerUnitInitColIndex);
        aiPlayer.setFormation(FormationFactory.getRandomFormation().init());
        aiPlayer.setMaxDeadCount(new SimpleIntegerProperty(aiPlayer.getFormation().getUnitCount() * 4 / 10));
        aiPlayer.createNewUnitQueue();

        peoplePlayer.setAiFlag(false);
        peoplePlayer.setEqColIndex(peoplePlayerEqColIndex);
        peoplePlayer.setUnitInitColIndex(peoplePlayerUnitInitColIndex);
        peoplePlayer.setFormation(FormationFactory.getRandomFormation().init());
        peoplePlayer.setMaxDeadCount(new SimpleIntegerProperty(peoplePlayer.getFormation().getUnitCount() * 6 / 10));
        peoplePlayer.createNewUnitQueue();

        playerDeque.add(aiPlayer);
        playerDeque.add(peoplePlayer);
    }

    public void handleDeadCount(ActionItem actionItem) {
        if (actionItem.isAiPlayer()) {
            int nowDeadCount = this.getAiPlayer().getDeadCount().get() + 1;
            this.getAiPlayer().getDeadCount().set(nowDeadCount);
        } else {
            int nowDeadCount = this.getPeoplePlayer().getDeadCount().get() + 1;
            this.getPeoplePlayer().getDeadCount().set(nowDeadCount);
        }
    }

    public void testGameOver() {
        // 生命值低于0游戏结束
        if (this.getPeoplePlayer().getHp().get() <= 0) {
            if (!this.isGameOverFlag()) {
                shouGameOverUI(false);
                return;
            }
        } else if (this.getAiPlayer().getHp().get() <= 0) {
            if (!this.isGameOverFlag()) {
                shouGameOverUI(true);
                return;
            }
        }

        // 伤亡超过最大值游戏结束
        if (this.getPeoplePlayer().getDeadCount().get() >= this.getPeoplePlayer().getMaxDeadCount().get()) {
            if (!this.isGameOverFlag()) {
                shouGameOverUI(false);
                return;
            }
        } else if (this.getAiPlayer().getDeadCount().get() >= this.getAiPlayer().getMaxDeadCount().get()) {
            if (!this.isGameOverFlag()) {
                shouGameOverUI(true);
                return;
            }
        }
    }

    private void shouGameOverUI(boolean peopleWinFlag) {
        Rectangle scrim = new Rectangle();
        scrim.widthProperty().bind(playGameSceneRoot.widthProperty());
        scrim.heightProperty().bind(playGameSceneRoot.heightProperty());
        scrim.setFill(Color.color(0, 0.5, 0, 0.8));
        BorderPane gameOverPane = new BorderPane();
        Label gameOverLabel;
        if (peopleWinFlag) {
            gameOverLabel = new Label("游戏结束！恭喜赢了！");
        } else {
            gameOverLabel = new Label("游戏结束！很遗憾输了！");
        }
        gameOverLabel.setBackground(StyleConstants.WHITE_BACKGROUND);
        gameOverLabel.setFont(StyleConstants.font24);

        VBox gameOverCenter = new VBox(10);
        gameOverCenter.setAlignment(Pos.CENTER);
        gameOverCenter.setPadding(new Insets(10));
        gameOverCenter.getChildren().addAll(gameOverLabel, showIndexSceneBtn);

        gameOverPane.setCenter(gameOverCenter);
        this.setGameOverFlag(true);
        Platform.runLater(() -> {
            playGameSceneRoot.getChildren().addAll(scrim, gameOverPane);
        });
    }

}
