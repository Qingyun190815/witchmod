package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MortusClaw extends AbstractWitchCard {
    public static final String ID = "MortusClaw";  // 卡片ID
    public static final String NAME = "Mortus Claw";  // 卡片名称
    public static final String IMG = "cards/mortusclaw.png";  // 卡片图片路径
    public static final String DESCRIPTION = "保留。 NL 造成 !D! 点伤害，然后敌人失去 !D! 点生命值。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 稀有度：不常见
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡片池

    private static final int COST = 3;  // 卡片能量消耗
    private static final int POWER = 12;  // 基础伤害
    private static final int UPGRADE_BONUS = 5;  // 升级后的伤害加成

    public MortusClaw() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
        this.retain = true;  // 设置卡片为保留状态，即不会在回合结束时丢弃
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        // 敌人失去生命值（即造成“HP_LOSS”类型伤害）
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, DamageType.HP_LOSS), AbstractGameAction.AttackEffect.POISON));
    }

    @Override
    public AbstractCard makeCopy() {
        return new MortusClaw();  // 返回卡片副本
    }

    @Override
    public void atTurnStart() {
        // 在回合开始时，保持该卡片
        retain = true;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级伤害
        }
    }
}
