package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.DarkProcessionPower;

public class DarkProcession extends AbstractWitchCard {
    public static final String ID = "DarkProcession";  // 卡片ID
    public static final String NAME = "Dark Procession";  // 卡片名称
    public static final String IMG = "cards/darkprocession.png";  // 卡片图片路径
    public static final String DESCRIPTION = "在你的回合开始时，将上一个使用的卡片的副本加入到你的手牌。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.POWER;  // 卡片类型：能力

    private static final int COST = 3;  // 初始能量消耗
    private static final int COST_UPGRADED = 2;  // 升级后的能量消耗

    public DarkProcession() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 为自己应用 "DarkProcessionPower" 力量，使得下回合开始时将上一个使用的卡片加入手牌
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DarkProcessionPower(1), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DarkProcession();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBaseCost(COST_UPGRADED);  // 升级卡片的能量消耗
        }
    }
}
