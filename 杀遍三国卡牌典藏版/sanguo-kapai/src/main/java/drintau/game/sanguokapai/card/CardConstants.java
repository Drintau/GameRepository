package drintau.game.sanguokapai.card;

public final class CardConstants {

    // 卡牌类型：装备、战术、单位
    public enum CardType {
        EQUIPMENT,TACTIC,UNIT;
    }

    public enum UnitType {
        // 射->枪、枪->骑，骑->甲，甲->术，术->盾，盾->射
        SHOOTER("射手"),   // 射 → 枪
        GUNNER("枪兵"),   // 枪 → 骑
        CAVALRY("骑兵"),    // 骑 → 甲
        ARMOR("甲士"),       // 甲 → 术
        MAGE("术士"),      // 术 → 盾
        SHIELD("盾卫");   // 盾 → 射

        public final String displayName;

        UnitType(String displayName) {
            this.displayName = displayName;
        }
    }

}
