package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BleedOut extends AbstractWitchCard {
    public static final String ID = "BleedOut";  // 卡片ID
    public static final String NAME = "Bleed Out";  // 卡片名称
    public static final String IMG = "cards/bleedout.png";  // 卡片图片路径
    public static final String DESCRIPTION = "ALL enemies lose !D! health.";  // 描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  // 目标：所有敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 池（数量）

    private static final int COST = 0;  // 能量消耗
    private static final int POWER = 4;  // 初始伤害
    private static final int UPGRADE_BONUS = 3;  // 升级后增加的伤害数值

    public BleedOut() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
        this.isMultiDamage = true;  // 启用多目标伤害
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用时对所有敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, multiDamage, DamageType.HP_LOSS, AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BleedOut();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级伤害
        }
    }
}
