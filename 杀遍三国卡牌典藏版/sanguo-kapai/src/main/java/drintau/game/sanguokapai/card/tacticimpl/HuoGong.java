package drintau.game.sanguokapai.card.tacticimpl;

import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.ActionItem;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.util.ThreadSleepUtil;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;

public class HuoGong extends TacticCard {

    @Override
    public void exec(PlayerData playerData, int rowIndex) {
        StackPane[][] cells = DesktopContext.getInstance().getCells();
        for (int i = DesktopContext.peoplePlayerUnitInitColIndex; i <= DesktopContext.aiPlayerUnitInitColIndex; i++) {
            StackPane cell = cells[rowIndex][i];
            if (!cell.getChildren().isEmpty()) {
                ActionItem targetCellActionItem = (ActionItem) cell.getUserData();
                if (playerData.isAiFlag() != targetCellActionItem.isAiPlayer()) {
                    if (targetCellActionItem.getUnitCard().getLevel() < 3) {
                        targetCellActionItem.setDeadFlag(true);
                        Platform.runLater(() -> {
                            cell.getChildren().clear();
                            cell.setUserData(null);
                        });
                        ThreadSleepUtil.sleepSeconds(1L);
                    }
                }
            }
        }
    }

    @Override
    public String getDescription() {
        return "火攻：消灭敌方一行等级1、2的单位";
    }

}
