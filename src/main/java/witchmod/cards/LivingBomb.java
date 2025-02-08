package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.actions.KillMonsterAction;

public class LivingBomb extends AbstractWitchCard {
    public static final String ID = "LivingBomb";  // 卡片ID
    public static final String NAME = "Living Bomb";  // 卡片名称
    public static final String IMG = "cards/livingbomb.png";  // 卡片图片路径
    public static final String DESCRIPTION = "若敌人的当前生命值低于 !M!，则击杀该敌人。若成功击杀，其他敌人将受到 !D! 点伤害。";  // 卡片描述
    private static final String DESCRIPTION_UPGRADE = "若敌人的当前生命值低于 !M!，则击杀该敌人。若成功击杀，其他敌人将受到 !D! 点伤害，升级后伤害增加。";  // 升级后描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 稀有度：不常见
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡片池

    private static final int COST = 3;  // 卡片能量消耗
    private static final int POWER = 15;  // 卡片的生命值阈值
    private static final int UPGRADED_BONUS = 5;  // 升级后的生命值阈值增加
    private static final int DAMAGE = 12;  // 卡片的基础伤害
    private static final int UPGRADED_BONUS_DAMAGE = 4;  // 升级后的伤害加成

    public LivingBomb() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWER;  // 设置基础魔力值，决定击杀敌人的生命值阈值
        this.baseDamage = DAMAGE;  // 设置基础伤害
        this.isMultiDamage = true;  // 设置为多重伤害
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.currentHealth > magicNumber) {
            // 如果敌人的生命值大于设定的阈值，则卡片无法使用
            return;
        }
        // 如果敌人的生命值低于阈值，击杀该敌人
        AbstractDungeon.actionManager.addToTop(new KillMonsterAction(m));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25f));  // 等待动作
        // 对所有其他敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, multiDamage, DamageType.THORNS, AttackEffect.FIRE));
    }

    @Override
    public AbstractCard makeCopy() {
        return new LivingBomb();  // 返回卡片副本
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (m != null && m.currentHealth > magicNumber) {
            // 如果敌人的生命值大于设定的阈值，显示不能使用的信息
            cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0] + magicNumber + cardStrings.EXTENDED_DESCRIPTION[1];
            return false;
        }
        return super.canUse(p, m);  // 调用父类方法
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(UPGRADED_BONUS);  // 升级时增加生命值阈值
            upgradeDamage(UPGRADED_BONUS_DAMAGE);  // 升级时增加伤害
            rawDescription = DESCRIPTION_UPGRADE;  // 更新卡片描述
            initializeDescription();  // 初始化描述
        }
    }
}
