package witchmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.actions.DrawFromDiscardPileAction;

public class MysticUnburial extends AbstractWitchCard {
    public static final String ID = "MysticUnburial";  // 卡片ID
    public static final String NAME = "Mystic Unburial";  // 卡片名称
    public static final String IMG = "cards/mysticunburial.png";  // 卡片图片路径
    public static final String DESCRIPTION = "从弃牌堆抽一张卡牌。 NL 弃牌。";  // 升级前卡片描述
    public static final String DESCRIPTION_UPGRADED = "从弃牌堆抽一张卡牌。该卡牌在本回合的费用为 0。 NL 弃牌。";  // 升级后卡片描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 稀有度：稀有
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 卡片池

    private static final int COST = 0;  // 卡片能量消耗：0

    public MysticUnburial() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.exhaust = true;  // 设置卡片为“弃牌”类型
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 执行从弃牌堆抽一张卡的动作
        AbstractDungeon.actionManager.addToBottom(new DrawFromDiscardPileAction(1, false, upgraded));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // 如果弃牌堆为空，卡片不能使用，显示提示信息
        if (p.discardPile.isEmpty()) {
            cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];  // 提示“弃牌堆为空”
            return false;
        }
        return true;
    }

    @Override
    public AbstractCard makeCopy() {
        return new MysticUnburial();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDescription();  // 升级卡片描述
        }
    }
}
