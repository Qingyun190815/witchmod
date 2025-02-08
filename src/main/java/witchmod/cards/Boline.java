package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Boline extends AbstractWitchCard {
    public static final String ID = "Boline";  // 卡片ID
    public static final String NAME = "Boline";  // 卡片名称
    public static final String IMG = "cards/boline.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Deal !D! damage. If this kills the enemy, obtain a random potion and Exhaust this card.";  // 描述
    public static final String DESCRIPTION_UPGRADED = "Retain. NL Deal !D! damage. If this kills the enemy, obtain a random potion and Exhaust this card.";  // 升级描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 池（数量）

    private static final int COST = 0;  // 能量消耗
    private static final int POWER = 6;  // 初始伤害
    private static final int POWER_UPGRADED_BONUS = 3;  // 升级后增加的伤害

    public Boline() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 如果这次攻击能够击杀敌人
        if ((damage >= m.currentHealth + m.currentBlock && damageTypeForTurn == DamageType.NORMAL) ||
                (damage >= m.currentHealth && damageTypeForTurn == DamageType.HP_LOSS)) {
            exhaust = true;  // 这张卡片会被消耗
            AbstractDungeon.actionManager.addToBottom(new ObtainPotionAction(PotionHelper.getRandomPotion()));  // 获得一个随机药水
        }
        // 对敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_VERTICAL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Boline();  // 返回卡片副本
    }

    @Override
    public void atTurnStart() {
        if (upgraded) {
            this.retain = true;  // 如果卡片已经升级，则保留它
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(POWER_UPGRADED_BONUS);  // 升级伤害
            upgradeDescription();  // 升级描述
            retain = true;  // 升级后这张卡片会被保留
        }
    }
}
