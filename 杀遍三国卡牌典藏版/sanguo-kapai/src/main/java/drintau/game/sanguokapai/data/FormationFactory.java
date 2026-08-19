package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.data.formation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class FormationFactory {

    private static final Map<String, Supplier<AbstractFormation>> FORMATION_MAP = new HashMap<>();

    static {
        FORMATION_MAP.put(FormationConstants.CHANG_SHE_ZHEN, ChangSheZhen::new);
        FORMATION_MAP.put(FormationConstants.YAN_XING_ZHEN, YanXingZhen::new);
        FORMATION_MAP.put(FormationConstants.CHONG_E_ZHEN, ChongEZhen::new);
    }

    public static AbstractFormation getFormation(String formationName){
        return FORMATION_MAP.get(formationName).get();
    }

}
