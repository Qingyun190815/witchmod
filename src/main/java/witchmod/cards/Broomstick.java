package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Broomstick extends AbstractWitchCard {
    public static final String ID = "Broomstick";  // 卡片ID
    public static final String NAME = "Broomstick";  // 卡片名称
    public static final String IMG = "cards/broomsticksmash.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 !D! 点伤害。如果目标是虚弱状态，伤害增加50%，并应用1层虚弱。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 卡片目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int COST = 1;  // 卡片能量消耗
    private static final int POWER = 8;  // 初始伤害
    private static final int UPGRADE_BONUS = 4;  // 升级后增加的伤害

    public Broomstick() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 如果目标有虚弱状态
        if (m.hasPower(WeakPower.POWER_ID)) {
            // 给目标增加1层虚弱
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1));
        }
        // 对目标造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Broomstick();  // 返回一张新副本
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
        if (mo == null) {
            return tmp;  // 如果没有敌人目标，则不修改伤害
        }
        // 如果目标有虚弱状态，增加25%的伤害
        float bonus = mo.getPower(WeakPower.POWER_ID) == null ? 1f : 1.25f;
        return tmp * bonus;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级伤害
        }
    }
}
