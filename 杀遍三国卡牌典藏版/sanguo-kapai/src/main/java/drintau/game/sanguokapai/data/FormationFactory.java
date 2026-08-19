package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.data.formation.*;
import drintau.game.sanguokapai.util.RandomUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class FormationFactory {

    private static final List<String> FORMATION_NAME_LIST = new ArrayList<>();

    private static final Map<String, Supplier<AbstractFormation>> FORMATION_MAP = new HashMap<>();

    static {
        FORMATION_NAME_LIST.add(FormationConstants.CHANG_SHE_ZHEN);
        FORMATION_NAME_LIST.add(FormationConstants.YAN_XING_ZHEN);
        FORMATION_NAME_LIST.add(FormationConstants.CHONG_E_ZHEN);

        FORMATION_MAP.put(FormationConstants.CHANG_SHE_ZHEN, ChangSheZhen::new);
        FORMATION_MAP.put(FormationConstants.YAN_XING_ZHEN, YanXingZhen::new);
        FORMATION_MAP.put(FormationConstants.CHONG_E_ZHEN, ChongEZhen::new);
    }

    public static AbstractFormation getFormation(String formationName){
        return FORMATION_MAP.get(formationName).get();
    }

    public static AbstractFormation getRandomFormation(){
        int randomInt = RandomUtil.randomInt(FORMATION_NAME_LIST.size());
        return getFormation(FORMATION_NAME_LIST.get(randomInt));
    }

}
