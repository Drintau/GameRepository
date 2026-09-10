package drintau.game.sanguokapai.data.formation;

import drintau.game.sanguokapai.card.UnitCard;
import drintau.game.sanguokapai.data.HeroData;
import drintau.game.sanguokapai.data.SoldierData;
import drintau.game.sanguokapai.util.RandomUtil;

import java.util.List;

public final class SuiJiZhen extends AbstractFormation {

    public SuiJiZhen() {
        super(FormationConstants.SUI_JI_ZHEN, 50);
    }

    @Override
    public AbstractFormation init() {
        List<UnitCard> allSoldiers = SoldierData.getAllSoldiers();
        List<UnitCard> allHeroes = HeroData.getAllHeroes();

        for (int i = 0; i < getUnitCount(); i++) {
            // 英雄概率20%
            if (RandomUtil.roll(RandomUtil.rate20)) {
                int heroIndex = RandomUtil.randomInt(allHeroes.size());
                getHeroList().add(allHeroes.get(heroIndex));
            } else {
                int soldierIndex = RandomUtil.randomInt(allSoldiers.size());
                getSoldierList().add(allSoldiers.get(soldierIndex));
            }
        }
        return this;
    }

    @Override
    public String getDescription() {
        return """
                %s
                单位数：%d（英雄：？，士兵：？）
                """.formatted(getName(), getUnitCount());
    }
}
