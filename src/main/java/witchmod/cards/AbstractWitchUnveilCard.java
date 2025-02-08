package witchmod.cards;

/**
 * 即时类卡牌
 */
public abstract class AbstractWitchUnveilCard extends AbstractWitchCard{
    public AbstractWitchUnveilCard(String id, String img, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(id, img, cost, type, rarity, target);
    }
}
