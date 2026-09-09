package drintau.game.sanguokapai.data.formation;

public final class SuiJiZhen extends AbstractFormation {

    public SuiJiZhen() {
        super(FormationConstants.SUI_JI_ZHEN, 50);
        this.setRandomFlag(true);
    }

    @Override
    public String getDescription() {
        return """
                %s
                单位数：%d（英雄：？，士兵：？）
                """.formatted(getName(), getUnitCount());
    }
}
