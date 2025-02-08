package witchmod.relics;


import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import witchmod.cards.AbstractWitchUnveilCard;

public class CustomRelicTest extends AbstractWitchRelic {

    private static final RelicTier TIER = RelicTier.STARTER; // 遗物等级（起始遗物）
    public static final String ID = "CustomRelicTest";  // 遗物的ID
    public static final String NAME = "Custom Relic";  // 遗物名称
    private static final String IMG = "relics/MagicHats.png";

    // 创建构造方法
    public CustomRelicTest() {
        super(ID, IMG, TIER, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0]; // 读取描述，支持多语言
    }
    /**
     * 每次新卡牌加入手牌时触发
     */
    @Override
    public void onCardDraw(AbstractCard card) {
        super.onCardDraw(card);  // 确保继承类的 `onCardDraw` 也被调用
        if (card instanceof AbstractWitchUnveilCard){
            applyEffect((AbstractWitchUnveilCard) card);
            flash();
        }

    }

    // 如果卡片存在，则执行相应效果（例如：为玩家增加力量）
    private void applyEffect(AbstractWitchUnveilCard  card) {
            card.unveil();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CustomRelicTest();  // 返回该遗物的副本
    }
}
