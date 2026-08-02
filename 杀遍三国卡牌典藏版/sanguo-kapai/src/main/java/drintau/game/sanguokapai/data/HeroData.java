package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.CardConstants;
import drintau.game.sanguokapai.card.UnitCard;

import java.util.List;

public final class HeroData {

    public static final UnitCard GUAN_YU = new UnitCard("关羽", CardConstants.UnitType.CAVALRY, 5, 4, 9);
    public static final UnitCard ZHANG_FEI = new UnitCard("张飞", CardConstants.UnitType.GUNNER, 5, 3, 8);
    public static final UnitCard MA_CHAO = new UnitCard("马超", CardConstants.UnitType.CAVALRY, 5, 5, 8);
    public static final UnitCard ZHAO_YUN = new UnitCard("赵云", CardConstants.UnitType.GUNNER, 5, 4, 8);
    public static final UnitCard HUANG_ZHONG = new UnitCard("黄忠", CardConstants.UnitType.SHOOTER, 5, 2, 7);

    public static List<UnitCard> getAllHeroes() {
        return List.of(
                GUAN_YU,
                ZHANG_FEI,
                MA_CHAO,
                ZHAO_YUN,
                HUANG_ZHONG
        );
    }

}