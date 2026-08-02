package drintau.game.sanguokapai.desktop.event;

import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import drintau.game.sanguokapai.util.RandomUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
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

        // 抽取卡牌，重新生成卡牌选项
        HBox cardSelectCenter = desktopContext.getCardSelectCenter();
        if (desktopContext.getCardList().size() < 5) {
            int createNum = 5 - desktopContext.getCardList().size();
            for (int i = 0; i < createNum; i++) {
                UnitCard randomUnit;
                int flag1 = RandomUtil.randomInt(10);
                // 英雄抽取概率20%
                if (flag1 <= 1) {
                    List<UnitCard> heroList = desktopContext.getHeroList();
                    int heroIndex = RandomUtil.randomInt(heroList.size());
                    randomUnit = heroList.get(heroIndex);
                } else {
                    List<UnitCard> soldierList = desktopContext.getSoldierList();
                    int soldierIndex = RandomUtil.randomInt(soldierList.size());
                    randomUnit = soldierList.get(soldierIndex);
                }

                ToggleButton cardBtn = new ToggleButton();
                cardBtn.setPrefSize(100,150);
                cardBtn.setUserData(randomUnit);
                Label cardInfoLabel = new Label(randomUnit.getDescription());
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
                desktopContext.getPlayer1().setSelectCard(selected);
            } else {
                desktopContext.getPlayer1().setSelectCard(null);
            }
        });
    }

}
