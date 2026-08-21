package drintau.game.sanguokapai.data.formation;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.HeroData;
import drintau.game.sanguokapai.data.SoldierData;
import drintau.game.sanguokapai.util.RandomUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阵型
 */
@Setter
@Getter
public abstract class AbstractFormation {

    private String name;
    private Integer unitCount;
    private Map<CardConstants.UnitType, Integer> unitTypeCountMap =  new HashMap<>();

    // init是给单位、英雄列表填充实际数据
    private List<UnitCard> soldierList = new ArrayList<>();
    private List<UnitCard> heroList = new ArrayList<>();

    public AbstractFormation(String name,  Integer unitCount) {
        this.name = name;
        this.unitCount = unitCount;
    }

    public AbstractFormation init(){
        // 士兵列表
        Map<CardConstants.UnitType, List<UnitCard>> unitTypeCardListMap = SoldierData.getUnitTypeCardListMap();
        for (Map.Entry<CardConstants.UnitType, Integer> unitTypeIntegerEntry : unitTypeCountMap.entrySet()) {
            CardConstants.UnitType key = unitTypeIntegerEntry.getKey();
            List<UnitCard> unitCardList = new ArrayList<>();
            while (unitCardList.size() < unitTypeIntegerEntry.getValue()) {
                unitCardList.addAll(unitTypeCardListMap.get(key));
            }
            soldierList.addAll(unitCardList);
        }

        // 英雄列表
        List<UnitCard> allHeroList = HeroData.getAllHeroes();
        for (int i = 0; i < unitCount - soldierList.size(); i++) {
            int heroIndex = RandomUtil.randomInt(allHeroList.size());
            heroList.add(allHeroList.get(heroIndex));
        }

        return this;
    }

    public String getDescription() {
        int soldierSum = unitTypeCountMap.values().stream().mapToInt(Integer::intValue).sum();

        StringBuilder sb = new StringBuilder();
        sb.append("""
            %s
            单位数：%d
            英雄数：%d
            士兵数：%d
            """.formatted(name, unitCount, unitCount - soldierSum, soldierSum));

        for (Map.Entry<CardConstants.UnitType, Integer> unitTypeIntegerEntry : unitTypeCountMap.entrySet()) {
            CardConstants.UnitType key = unitTypeIntegerEntry.getKey();
            int unitCount = unitTypeIntegerEntry.getValue();
            sb.append("""
                    %s：%d
                    """.formatted(key.displayName, unitCount));
        }

        return sb.toString();
    }

}
