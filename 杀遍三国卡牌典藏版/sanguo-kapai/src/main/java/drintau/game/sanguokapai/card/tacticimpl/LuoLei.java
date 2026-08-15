package drintau.game.sanguokapai.card.tacticimpl;

import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.data.PlayerData;
import drintau.game.sanguokapai.desktop.ActionItem;
import drintau.game.sanguokapai.desktop.DesktopContext;
import drintau.game.sanguokapai.desktop.StyleConstants;
import drintau.game.sanguokapai.util.DaemonScheduler;
import drintau.game.sanguokapai.util.RandomUtil;
import drintau.game.sanguokapai.util.ThreadSleepUtil;
import javafx.application.Platform;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LuoLei extends TacticCard {

    @Override
    public void exec(PlayerData playerData, int rowIndex) {
        DesktopContext.getInstance().getSelectCardBtn().setDisable(true);
        DesktopContext.getInstance().getEndTurnBtn().setDisable(true);
        DaemonScheduler.getInstance().submitOnceDelayTask(() -> {
            StackPane[][] cells = DesktopContext.getInstance().getCells();
            if (playerData.isAiFlag()) {
                // 电脑执行，从右到左
                for (int i = DesktopContext.aiPlayerUnitInitColIndex; i >= DesktopContext.peoplePlayerUnitInitColIndex; i--) {
                    StackPane cell = cells[rowIndex][i];
                    if (!cell.getChildren().isEmpty()) {
                        ActionItem targetCellActionItem = (ActionItem) cell.getUserData();
                        if (playerData.isAiFlag() != targetCellActionItem.isAiPlayer()) {
                            Platform.runLater(() -> {
                                cell.setBorder(StyleConstants.CELL_BORDER_ACTION);
                            });
                            ThreadSleepUtil.sleepSeconds(1L);
                            log.info("落雷计策卡：单位生效判定");
                            if (RandomUtil.roll(RandomUtil.rate50)) {
                                targetCellActionItem.setDeadFlag(true);
                                Platform.runLater(() -> {
                                    cell.getChildren().clear();
                                    cell.setUserData(null);
                                    cell.setBorder(StyleConstants.CELL_BORDER_DEFAULT);
                                });
                            } else {
                                Platform.runLater(() -> {
                                    cell.setBorder(StyleConstants.CELL_BORDER_DEFAULT);
                                });
                            }
                            ThreadSleepUtil.sleepSeconds(1L);
                        }
                    }
                }
                // 唤醒电脑行动线程
                synchronized (DesktopContext.getInstance().getAiActionLock()) {
                    DesktopContext.getInstance().getAiActionLock().notify();
                }
            } else {
                // 玩家执行，从左到右
                for (int i = DesktopContext.peoplePlayerUnitInitColIndex; i <= DesktopContext.aiPlayerUnitInitColIndex; i++) {
                    StackPane cell = cells[rowIndex][i];
                    if (!cell.getChildren().isEmpty()) {
                        ActionItem targetCellActionItem = (ActionItem) cell.getUserData();
                        if (playerData.isAiFlag() != targetCellActionItem.isAiPlayer()) {
                            Platform.runLater(() -> {
                                cell.setBorder(StyleConstants.CELL_BORDER_ACTION);
                            });
                            ThreadSleepUtil.sleepSeconds(1L);
                            log.info("落雷计策卡：单位生效判定");
                            if (RandomUtil.roll(RandomUtil.rate50)) {
                                targetCellActionItem.setDeadFlag(true);
                                Platform.runLater(() -> {
                                    cell.getChildren().clear();
                                    cell.setUserData(null);
                                    cell.setBorder(StyleConstants.CELL_BORDER_DEFAULT);
                                });
                            } else {
                                Platform.runLater(() -> {
                                    cell.setBorder(StyleConstants.CELL_BORDER_DEFAULT);
                                });
                            }
                            ThreadSleepUtil.sleepSeconds(1L);
                        }
                    }
                }
                // 玩家继续选择卡牌
                ThreadSleepUtil.sleepSeconds(1L);
                Platform.runLater(() -> {
                    DesktopContext.getInstance().getSelectCardBtn().setDisable(false);
                    DesktopContext.getInstance().getEndTurnBtn().setDisable(false);
                });
            }
        }, 1L, TimeUnit.SECONDS);
    }

    @Override
    public String getDescription() {
        return "落雷：一行范围内，敌方每个单位有50%概率会被消灭";
    }

    @Override
    public int suggestRow(PlayerData playerData) {
        // 只有电脑调用
        if (playerData.isAiFlag()) {
            if (!suggestSuccess()) {
                return super.suggestRow(playerData);
            }
            Map<Integer, Integer> rowEnemyCountMap =  new HashMap<>();
            StackPane[][] cells = DesktopContext.getInstance().getCells();
            for (int rowIndex = 0; rowIndex < DesktopContext.rows; rowIndex++) {
                for (int colIndex = DesktopContext.peoplePlayerUnitInitColIndex; colIndex <= DesktopContext.aiPlayerUnitInitColIndex; colIndex++) {
                    StackPane cell = cells[rowIndex][colIndex];
                    if (!cell.getChildren().isEmpty()) {
                        ActionItem targetCellActionItem = (ActionItem) cell.getUserData();
                        if (playerData.isAiFlag() != targetCellActionItem.isAiPlayer()) {
                            // 敌人数+1
                            rowEnemyCountMap.merge(rowIndex, 1, Integer::sum);
                        }
                    }
                }
            }
            if (rowEnemyCountMap.isEmpty()) {
                return super.suggestRow(playerData);
            }
            int maxEnemyCount = 0;
            int maxEnemyCountRowIndex = 3;
            for (Map.Entry<Integer, Integer> integerIntegerEntry : rowEnemyCountMap.entrySet()) {
                Integer rowIndex = integerIntegerEntry.getKey();
                Integer enemyCount = integerIntegerEntry.getValue();
                if (enemyCount > maxEnemyCount) {
                    maxEnemyCountRowIndex = rowIndex;
                    maxEnemyCount = enemyCount;
                }
            }
            return maxEnemyCountRowIndex;
        } else {
            return super.suggestRow(playerData);
        }
    }

    private boolean suggestSuccess() {
        log.info("落雷计策卡：电脑聪明执行判定");
        return RandomUtil.roll(RandomUtil.rate80);
    }

}
