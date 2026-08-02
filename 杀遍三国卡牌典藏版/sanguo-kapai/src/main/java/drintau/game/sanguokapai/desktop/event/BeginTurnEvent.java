package drintau.game.sanguokapai.desktop.event;

import drintau.game.sanguokapai.card.AbstractCard;
import drintau.game.sanguokapai.card.EquipmentCard;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.ActionItem;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import drintau.game.sanguokapai.util.RandomUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BeginTurnEvent implements EventHandler<ActionEvent> {

    @Override
    public void handle(ActionEvent event) {
        DesktopContext desktopContext = DesktopContext.getInstance();

        desktopContext.getBeginTurn().setDisable(true);
        desktopContext.getSelectCard().setDisable(false);
        desktopContext.getEndTurn().setDisable(false);

        desktopContext.getPeoplePlayer().setCurTurnPutUnitCardFlag(false);
        desktopContext.getPeoplePlayer().setCurTurnPutEqCardFlag(false);

        // 轮流行动
        for (PlayerData playerData : desktopContext.getPlayerDeque()) {
            if (playerData.isAiFlag()) {
                aiPlayGame();
            } else {
                peoplePlayGame();
            }
        }
    }

    private void aiPlayGame() {
        DesktopContext desktopContext = DesktopContext.getInstance();
        StackPane[][] cells = desktopContext.getCells();

        AbstractCard randomCard = getRandomCard();
        int rowIndex = RandomUtil.randomInt(3);

        if (randomCard instanceof UnitCard randomUnit) {
            int aiUnitInitColIndex = desktopContext.getAiPlayer().getUnitInitColIndex();
            Label label = new Label(randomUnit.getDescription());
            label.setBackground(StyleConstants.RED_BACKGROUND);
            label.setFont(StyleConstants.font16);
            cells[rowIndex][aiUnitInitColIndex].getChildren().add(label);
            desktopContext.getActionDeque().add(new ActionItem(true, rowIndex, aiUnitInitColIndex, randomUnit));
        } else if (randomCard instanceof EquipmentCard randomEq) {
            int aiEqColIndex = desktopContext.getAiPlayer().getEqColIndex();
            Label label = new Label(randomEq.getDescription());
            label.setBackground(StyleConstants.WHITE_BACKGROUND);
            label.setFont(StyleConstants.font16);
            cells[rowIndex][aiEqColIndex].getChildren().clear();
            cells[rowIndex][aiEqColIndex].getChildren().add(label);
        }

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
                }
                cardInfoLabel.setFont(StyleConstants.font16);
                cardBtn.setGraphic(cardInfoLabel);

                desktopContext.getCardList().add(cardBtn);
            }
        }

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
    }

    private AbstractCard getRandomCard() {
        AbstractCard randomCard;
        int randomInt = RandomUtil.randomInt(100);

        if (randomInt < 10) {
            // 英雄抽取概率10%
            List<UnitCard> heroList = DesktopContext.getInstance().getHeroList();
            int heroIndex = RandomUtil.randomInt(heroList.size());
            randomCard = heroList.get(heroIndex);
        } else if (randomInt < 20) {
            // 装备抽取概率
            List<EquipmentCard> equipmentList = DesktopContext.getInstance().getEquipmentList();
            int eqIndex = RandomUtil.randomInt(equipmentList.size());
            randomCard = equipmentList.get(eqIndex);
        } else {
            List<UnitCard> soldierList = DesktopContext.getInstance().getSoldierList();
            int soldierIndex = RandomUtil.randomInt(soldierList.size());
            randomCard = soldierList.get(soldierIndex);
        }
        return randomCard;
    }

}
