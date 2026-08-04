package drintau.game.sanguokapai.desktop.event;

import drintau.game.sanguokapai.card.AbstractCard;
import drintau.game.sanguokapai.card.EquipmentCard;
import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.ActionItem;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import drintau.game.sanguokapai.util.DaemonScheduler;
import drintau.game.sanguokapai.util.RandomUtil;
import drintau.game.sanguokapai.util.ThreadSleepUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BeginTurnEvent implements EventHandler<ActionEvent> {

    private final int rate100 = 100;
    private final int rate50 = 50;
    private final int rate30 = 30;
    private final int rate10 = 10;
    private final int rate0 = 0;

    @Override
    public void handle(ActionEvent event) {
        DesktopContext desktopContext = DesktopContext.getInstance();

        desktopContext.getBeginTurn().setDisable(true);

        desktopContext.getPeoplePlayer().setCurTurnPutUnitCardFlag(false);
        desktopContext.getPeoplePlayer().setCurTurnPutEqCardFlag(false);
        desktopContext.getPeoplePlayer().setCurTurnPutTacticCardFlag(false);

        // 轮流行动
        DaemonScheduler.getInstance().submitOnceDelayTask(() -> {
            for (PlayerData playerData : desktopContext.getPlayerDeque()) {
                if (playerData.isAiFlag()) {
                    aiPlayGame();
                } else {
                    peoplePlayGame();
                }
            }
        }, 1L, TimeUnit.SECONDS);
    }

    private void aiPlayGame() {
        DesktopContext desktopContext = DesktopContext.getInstance();
        StackPane[][] cells = desktopContext.getCells();

        // 抽计策
        TacticCard randomTactic = getRandomTactic();
        if (randomTactic != null) {
            // 计策界面
            BorderPane execTacticRoot = new BorderPane();
            execTacticRoot.setPadding(new Insets(10));
            execTacticRoot.setBackground(StyleConstants.WHITE_BACKGROUND);
            execTacticRoot.setPrefWidth(300);
            execTacticRoot.setPrefHeight(220);
            execTacticRoot.setMinSize(300, 220);
            execTacticRoot.setMaxSize(300, 220);

            Label execTacticTitle = new Label("电脑执行计策");
            execTacticTitle.setFont(StyleConstants.font24);
            execTacticRoot.setTop(execTacticTitle);
            BorderPane.setAlignment(execTacticTitle, Pos.CENTER);

            Label execTacticCenter = new Label(randomTactic.getDescription());
            execTacticCenter.setFont(StyleConstants.font16);
            execTacticRoot.setCenter(execTacticCenter);

            HBox execTacticBottom = new HBox(10);
            execTacticBottom.setAlignment(Pos.CENTER);
            Button execTacticSureButton = new Button("确认");
            execTacticSureButton.setFont(StyleConstants.font20);
            execTacticSureButton.setOnAction(e -> {
                randomTactic.exec(DesktopContext.getInstance().getAiPlayer(), RandomUtil.randomInt(3));
                desktopContext.getRoot().getChildren().remove(execTacticRoot);
            });
            execTacticBottom.getChildren().addAll(execTacticSureButton);
            execTacticRoot.setBottom(execTacticBottom);
            Platform.runLater(() -> {
                desktopContext.getRoot().getChildren().add(execTacticRoot);
                DaemonScheduler.getInstance().submitOnceDelayTask(() -> {
                    Platform.runLater(execTacticSureButton::fire);
                }, 2L, TimeUnit.SECONDS);
            });

            ThreadSleepUtil.sleepSeconds(4L);
        }

        int rowIndex = RandomUtil.randomInt(DesktopContext.rows);
        // 抽装备
        EquipmentCard randomEquipment = getRandomEquipment();
        if (randomEquipment != null) {
            int aiEqColIndex = desktopContext.getAiPlayer().getEqColIndex();
            int finalRowIndex = rowIndex;
            Platform.runLater(() -> {
                cells[finalRowIndex][aiEqColIndex].getChildren().clear();
                cells[finalRowIndex][aiEqColIndex].setUserData(null);
                Label label = new Label(randomEquipment.getDescription());
                label.setBackground(StyleConstants.WHITE_BACKGROUND);
                label.setFont(StyleConstants.font16);
                cells[finalRowIndex][aiEqColIndex].getChildren().add(label);
                cells[finalRowIndex][aiEqColIndex].setUserData(randomEquipment);
            });
            ThreadSleepUtil.sleepSeconds(1L);
        }

        rowIndex = RandomUtil.randomInt(DesktopContext.rows);
        // 抽单位
        UnitCard randomUnit = getRandomUnit();
        int aiUnitInitColIndex = desktopContext.getAiPlayer().getUnitInitColIndex();
        ActionItem actionItem = new ActionItem(true, rowIndex, aiUnitInitColIndex, randomUnit);
        int finalRowIndex = rowIndex;
        Platform.runLater(() -> {
            cells[finalRowIndex][aiUnitInitColIndex].getChildren().clear();
            cells[finalRowIndex][aiUnitInitColIndex].setUserData(null);
            Label label = new Label(randomUnit.getDescription());
            label.setBackground(StyleConstants.RED_BACKGROUND);
            label.setFont(StyleConstants.font16);
            cells[finalRowIndex][aiUnitInitColIndex].getChildren().add(label);
            cells[finalRowIndex][aiUnitInitColIndex].setUserData(actionItem);
        });
        desktopContext.getActionDeque().add(actionItem);
    }

    private void peoplePlayGame() {
        DesktopContext desktopContext = DesktopContext.getInstance();
        // 抽取卡牌，重新生成卡牌选项
        HBox cardSelectCenter = desktopContext.getCardSelectCenter();
        if (desktopContext.getCardList().size() < 5) {
            int createNum = 5 - desktopContext.getCardList().size();
            for (int i = 0; i < createNum; i++) {
                AbstractCard randomCard = getRandomCard();

                ToggleButton cardBtn = new ToggleButton();
                cardBtn.setPrefSize(100,150);
                cardBtn.setUserData(randomCard);
                Label cardInfoLabel = new Label(randomCard.getDescription());
                if (randomCard instanceof UnitCard) {
                    cardInfoLabel.setBackground(StyleConstants.PLAYER_UNIT_BACKGROUND);
                } else if (randomCard instanceof EquipmentCard) {
                    cardInfoLabel.setBackground(StyleConstants.WHITE_BACKGROUND);
                } else if (randomCard instanceof TacticCard) {
                    cardInfoLabel.setBackground(StyleConstants.PLAYER_TACTIC_BACKGROUND);
                    cardInfoLabel.setWrapText(true);
                }
                cardInfoLabel.setFont(StyleConstants.font16);
                cardBtn.setGraphic(cardInfoLabel);

                desktopContext.getCardList().add(cardBtn);
            }
        }

        Platform.runLater(() -> {
            cardSelectCenter.getChildren().clear();
            ToggleGroup cardSelectGroup = new ToggleGroup();
            for (int i = 0; i < desktopContext.getCardList().size(); i++) {
                ToggleButton cardBtn = desktopContext.getCardList().get(i);
                cardBtn.setToggleGroup(cardSelectGroup);
                cardSelectCenter.getChildren().add(cardBtn);
            }
            cardSelectGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    ToggleButton selected = (ToggleButton) newVal;
                    desktopContext.getPeoplePlayer().setSelectCard(selected);
                } else {
                    desktopContext.getPeoplePlayer().setSelectCard(null);
                }
            });

            desktopContext.getSelectCard().setDisable(false);
            desktopContext.getEndTurn().setDisable(false);
        });
    }

    private AbstractCard getRandomCard() {
        AbstractCard randomCard;
        int randomInt = RandomUtil.randomInt(rate100);

        if (randomInt < 10) {
            // 策略抽取概率
            List<TacticCard> tacticList = DesktopContext.getInstance().getTacticList();
            int tacticIndex = RandomUtil.randomInt(tacticList.size());
            randomCard = tacticList.get(tacticIndex);
        } else if (randomInt < 30) {
            // 装备抽取概率
            List<EquipmentCard> equipmentList = DesktopContext.getInstance().getEquipmentList();
            int eqIndex = RandomUtil.randomInt(equipmentList.size());
            randomCard = equipmentList.get(eqIndex);
        } else if (randomInt < 40) {
            // 英雄抽取概率
            List<UnitCard> heroList = DesktopContext.getInstance().getHeroList();
            int heroIndex = RandomUtil.randomInt(heroList.size());
            randomCard = heroList.get(heroIndex);
        } else {
            // 基础战斗单位抽取概率
            List<UnitCard> soldierList = DesktopContext.getInstance().getSoldierList();
            int soldierIndex = RandomUtil.randomInt(soldierList.size());
            randomCard = soldierList.get(soldierIndex);
        }
        return randomCard;
    }

    private UnitCard getRandomUnit() {
        UnitCard randomUnit;
        int randomInt = RandomUtil.randomInt(rate100);
        // 英雄抽取概率
        if (randomInt < rate10) {
            List<UnitCard> heroList = DesktopContext.getInstance().getHeroList();
            int heroIndex = RandomUtil.randomInt(heroList.size());
            randomUnit = heroList.get(heroIndex);
        } else {
            List<UnitCard> soldierList = DesktopContext.getInstance().getSoldierList();
            int soldierIndex = RandomUtil.randomInt(soldierList.size());
            randomUnit = soldierList.get(soldierIndex);
        }
        return randomUnit;
    }

    private EquipmentCard getRandomEquipment() {
        EquipmentCard randomEq = null;
        int randomInt = RandomUtil.randomInt(rate100);
        // 抽装备概率
        if (randomInt < rate50) {
            List<EquipmentCard> equipmentList = DesktopContext.getInstance().getEquipmentList();
            int eqIndex = RandomUtil.randomInt(equipmentList.size());
            randomEq = equipmentList.get(eqIndex);
        }
        return randomEq;
    }

    private TacticCard getRandomTactic() {
        TacticCard randomTactic = null;
        int randomInt = RandomUtil.randomInt(rate100);
        if (randomInt < rate10) {
            List<TacticCard> tacticList = DesktopContext.getInstance().getTacticList();
            int tacticIndex = RandomUtil.randomInt(tacticList.size());
            randomTactic = tacticList.get(tacticIndex);
        }
        return randomTactic;
    }

}
