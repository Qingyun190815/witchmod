package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.SetDontTriggerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class IllusionOfStrengthCurse extends AbstractWitchCard {
    public static final String ID = "IllusionOfStrengthCurse";
    public static final String NAME = "Delusion of Strength";  // 卡片名称
    public static final String IMG = "cards/delusionofstrength.png";  // 卡片图片路径
    public static final String DESCRIPTION = "不可玩。在你的回合结束时，失去 !M! 点力量，或者如果你拥有 !M! 点或更少的力量，则此卡被消耗。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.SPECIAL;  // 稀有度：特殊
    private static final CardTarget TARGET = CardTarget.NONE;  // 目标：无
    private static final CardType TYPE = CardType.CURSE;  // 卡片类型：诅咒

    private static final int POOL = 2;  // 卡片池
    private static final int COST = -2;  // 能量消耗：不可玩

    private static final int POWER = 2;  // 默认力量数值

    public IllusionOfStrengthCurse() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWER;  // 设置魔法数为力量数值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 如果玩家拥有蓝色蜡烛遗物，触发蓝色蜡烛效果
        if (!dontTriggerOnUseCard && p.hasRelic("Blue Candle")) {
            useBlueCandle(p);
        } else {
            // 否则失去指定点数的力量
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new StrengthPower(p, -magicNumber), -magicNumber));
            AbstractDungeon.actionManager.addToBottom(new SetDontTriggerAction(this, false));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        dontTriggerOnUseCard = true;  // 禁用触发
        // 如果玩家的力量小于或等于指定值，或者没有力量，则消耗此卡
        if ((AbstractDungeon.player.hasPower(StrengthPower.POWER_ID) && AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount <= magicNumber) ||
                !(AbstractDungeon.player.hasPower(StrengthPower.POWER_ID))) {
            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));  // 消耗此卡
        } else {
            // 否则，将此卡加入卡片队列，待下次回合使用
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
        }
    }

    @Override
    public void triggerWhenDrawn() {
        AbstractDungeon.actionManager.addToBottom(new SetDontTriggerAction(this, false));  // 当卡片被抽取时，禁用触发
    }

    public AbstractCard makeCopy() {
        return new IllusionOfStrengthCurse();  // 返回卡片副本
    }

    public void upgrade() {
        // 该卡片没有升级效果
    }
}
