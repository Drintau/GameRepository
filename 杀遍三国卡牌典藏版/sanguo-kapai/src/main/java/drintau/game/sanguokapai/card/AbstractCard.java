package drintau.game.sanguokapai.card;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractCard {

    private CardConstants.CardType cardType;

    public abstract String getDescription();

}
