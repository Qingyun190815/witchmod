package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;  // 引入攻击效果类，定义攻击的视觉效果
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;  // 引入应用效果的动作类
import com.megacrit.cardcrawl.actions.common.DamageAction;  // 引入伤害动作类
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入卡牌类
import com.megacrit.cardcrawl.cards.DamageInfo;  // 引入伤害信息类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;  // 引入怪物类
import witchmod.powers.DecrepitPower;  // 引入自定义的效果类 DecrepitPower

/**
 * Zombie Spit 卡牌：造成伤害并应用 Decrepit 效果（使怪物受到持续削弱）。
 */
public class ZombieSpit extends AbstractWitchCard {
    // 卡牌的基本信息
    public static final String ID = "ZombieSpit";  // 唯一ID
    public static final String NAME = "Zombie Spit";  // 卡牌名称
    public static final String IMG = "cards/zombiespit.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "Deal !D! damage and apply !M! Decrepit.";  // 描述：造成 !D! 点伤害并应用 !M! 点 Decrepit 效果。

    private static final CardRarity RARITY = CardRarity.BASIC;  // 稀有度：基础卡牌（BASIC）
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人（ENEMY）
    private static final CardType TYPE = CardType.ATTACK;  // 卡牌类型：攻击（ATTACK）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int COST = 0;  // 能量消耗：0 点

    private static final int POWER = 4;  // 初始伤害值
    private static final int UPGRADED_BONUS = 2;  // 升级后增加的伤害值

    private static final int MAGIC = 1;  // 初始 Decrepit 效果值
    private static final int UPGRADED_MAGIC = 2;  // 升级后增加的 Decrepit 效果值

    /**
     * 构造函数：初始化卡牌属性
     */
    public ZombieSpit() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
        this.baseMagicNumber = this.magicNumber = MAGIC;  // 设置基础 Decrepit 效果值
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 对敌人造成伤害并应用 Decrepit 效果
     * @param p 玩家
     * @param m 目标怪物
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害，并设置伤害类型为“毒性”（POISON）效果
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.POISON));
        // 对怪物应用 Decrepit 效果，持续削弱怪物
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new DecrepitPower(m, magicNumber, false), magicNumber, true));
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 ZombieSpit 实例
     */
    public AbstractCard makeCopy() {
        return new ZombieSpit();
    }

    /**
     * 升级卡牌
     * - 增加伤害并提升 Decrepit 效果的强度
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();  // 升级卡牌名称，通常在名称后加“+”符号
            upgradeDamage(UPGRADED_BONUS);  // 升级后增加伤害
            upgradeMagicNumber(UPGRADED_MAGIC);  // 升级后增加 Decrepit 效果的强度
        }
    }
}
