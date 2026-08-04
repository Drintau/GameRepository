package drintau.game.sanguokapai.data;

import drintau.game.sanguokapai.card.TacticCard;
import drintau.game.sanguokapai.card.tacticimpl.AnJianShangRen;
import drintau.game.sanguokapai.card.tacticimpl.HuoGong;
import drintau.game.sanguokapai.card.tacticimpl.LuoLei;
import drintau.game.sanguokapai.card.tacticimpl.XiuYangShengXi;

import java.util.List;

public final class TacticData {

    public static final TacticCard XIU_YANG_SHENG_XI = new XiuYangShengXi();
    public static final TacticCard AN_JIAN_SHANG_REN = new AnJianShangRen();
    public static final TacticCard HUO_GONG = new HuoGong();
    public static final TacticCard LUO_LEI = new LuoLei();

    public static List<TacticCard> getAllTactics() {
        return List.of(
//                XIU_YANG_SHENG_XI,
//                AN_JIAN_SHANG_REN,
//                HUO_GONG,
                LUO_LEI
        );
    }
}
