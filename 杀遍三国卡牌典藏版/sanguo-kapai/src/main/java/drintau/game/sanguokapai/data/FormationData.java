package drintau.game.sanguokapai.data;

import java.util.HashMap;
import java.util.Map;

public final class FormationData {

    private final Map<String, Formation> formationMap =  new HashMap<>();

    public FormationData() {
        Formation changSheZhen = new Formation("长蛇阵");

        Formation heYiZhen = new Formation("鹤翼阵");

        Formation yanXingZhen = new Formation("雁形阵");
    }
}
