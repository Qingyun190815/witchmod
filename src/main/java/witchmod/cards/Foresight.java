package witchmod.cards;

import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.actions.ForesightAction;

public class Foresight extends AbstractWitchCard {
    public static final String ID = "Foresight";  // 卡片ID
    public static final String NAME = "Foresight";  // 卡片名称
    public static final String IMG = "cards/foresight.png";  // 卡片图片路径
    public static final String DESCRIPTION = "观看 2 张牌，然后抽一张牌。如果是诅咒或状态牌，则将其消耗。否则抽一张该牌的副本。消耗。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 卡池数量
    private static final int COST = 1;  // 初始能量消耗
    private static final int COST_UPGRADED = 0;  // 升级后的能量消耗

    public Foresight() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.exhaust = true;  // 设置卡片为消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 观看2张牌
        AbstractDungeon.actionManager.addToBottom(new ScryAction(2));
        // 执行自定义的ForesightAction
        AbstractDungeon.actionManager.addToBottom(new ForesightAction());
    }

    @Override
    public AbstractCard makeCopy() {
        return new Foresight();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBaseCost(COST_UPGRADED);  // 升级后降低能量消耗
            initializeDescription();  // 更新卡片描述
        }
    }
}
