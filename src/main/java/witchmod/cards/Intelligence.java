package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.IntelligencePower;

public class Intelligence extends AbstractWitchCard {
    public static final String ID = "Intelligence";  // 卡片ID
    public static final String NAME = "Intelligence";  // 卡片名称
    public static final String IMG = "cards/intelligence.png";  // 卡片图片路径
    public static final String DESCRIPTION = "在你的回合开始时，从你的弃牌堆中随机抽取 !M! 张卡牌。";  // 单数描述
    public static final String DESCRIPTION_PLURAL = "在你的回合开始时，从你的弃牌堆中随机抽取 !M! 张卡牌。";  // 复数描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 稀有度：稀有
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.POWER;  // 卡片类型：力量

    private static final int POOL = 1;  // 卡片池

    private static final int COST = 1;  // 能量消耗

    private static final int POWER = 1;  // 默认魔法数
    private static final int UPGRADE_BONUS = 1;  // 升级后的魔法数加成

    public Intelligence() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER;  // 设置魔法数
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 应用力量效果，启动“IntelligencePower”效果
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IntelligencePower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Intelligence();  // 返回卡片副本
    }

    @Override
    protected void upgradeMagicNumber(int amount) {
        super.upgradeMagicNumber(amount);  // 升级魔法数
        // 根据魔法数数量更新卡片描述
        if (magicNumber == 1) {
            rawDescription = DESCRIPTION;  // 单数描述
        } else {
            rawDescription = DESCRIPTION_PLURAL;  // 复数描述
        }
        initializeDescription();  // 初始化描述
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级魔法数
        }
    }
}
