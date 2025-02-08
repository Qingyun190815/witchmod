package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlackShield extends AbstractWitchCard {
    public static final String ID = "BlackShield";  // 卡片ID
    public static final String NAME = "Black Shield";  // 卡片名称
    public static final String IMG = "cards/blackshield.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Gain !B! Block, doubled if you have at least a Curse in hand.";  // 描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 池（数量）

    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 7;  // 初始护盾
    private static final int UPGRADE_BONUS = 2;  // 升级后增加的护盾数值

    public BlackShield() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置卡片基础护盾数值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int finalBlock = block;  // 初始护盾值

        // 检查玩家手牌中是否有诅咒卡
        for (AbstractCard c : p.hand.group) {
            if (c.type == CardType.CURSE) {
                finalBlock += block;  // 如果有诅咒卡，护盾翻倍
                break;  // 一旦找到诅咒卡就跳出循环
            }
        }

        // 为玩家提供最终的护盾
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, finalBlock));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackShield();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(UPGRADE_BONUS);  // 升级护盾数值
        }
    }
}
