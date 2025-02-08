package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.ImpendingDoomPower;

public class ImpendingDoom extends AbstractWitchCard {
    public static final String ID = "DoomBlade";  // 卡片ID
    public static final String NAME = "Impending Doom";  // 卡片名称
    public static final String IMG = "cards/impendingdoom.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 !D! 点伤害。下回合敌人失去 !D! 点生命。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 稀有度：稀有
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡片池

    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 8;  // 默认伤害
    private static final int UPGRADE_BONUS = 3;  // 升级后伤害加成

    public ImpendingDoom() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        // 给敌人应用即将到来的灾难效果（损失生命）
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new ImpendingDoomPower(m, p, damage), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ImpendingDoom();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        // 升级后，增加伤害
        if (!upgraded) {
            upgradeName();  // 升级名称
            upgradeDamage(UPGRADE_BONUS);  // 升级伤害
        }
    }
}
