package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;  // 引入攻击特效的枚举
import com.megacrit.cardcrawl.actions.animations.VFXAction;  // 引入VFX动作，用于播放特效
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;  // 引入应用力量的动作
import com.megacrit.cardcrawl.actions.common.DamageAction;  // 引入造成伤害的动作
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入卡牌的基础类
import com.megacrit.cardcrawl.cards.DamageInfo;  // 引入伤害信息类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入游戏中的地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;  // 引入怪物类
import com.megacrit.cardcrawl.powers.WeakPower;  // 引入虚弱状态的力量类
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;  // 引入闪电特效

/**
 * Thundercloud（雷云）卡牌：
 * - 使用时，给一个随机敌人施加虚弱（Weak），并对所有虚弱的敌人造成伤害。
 */
public class Thundercloud extends AbstractWitchUnveilCard {
    // 卡牌的基础信息
    public static final String ID = "Thundercloud";  // 唯一ID
    public static final String NAME = "Thundercloud";  // 卡牌名称
    public static final String IMG = "cards/thundercloud.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "When drawn apply !M! Weak to a random enemy. NL Deal !D! damage to all Weak enemies.";  // 描述：抽到时对一个随机敌人施加虚弱，之后对所有虚弱敌人造成伤害。

    private static final CardRarity RARITY = CardRarity.COMMON;  // 稀有度：普通（COMMON）
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  // 目标：所有敌人（ALL_ENEMY）
    private static final CardType TYPE = CardType.ATTACK;  // 卡牌类型：攻击（ATTACK）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int COST = 2;  // 能量消耗：2 点
    private static final int POWER = 12;  // 初始伤害：12
    private static final int UPGRADED_BONUS = 3;  // 升级后的伤害加成：+3

    private static final int MAGIC = 1;  // 魔法值：虚弱效果的强度（每次施加虚弱时，虚弱的强度为 1）

    /**
     * 构造函数：初始化卡牌属性
     */
    public Thundercloud() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = MAGIC;  // 设置魔法数值，控制虚弱强度
        this.baseDamage = POWER;  // 设置基础伤害
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 对所有虚弱的敌人造成伤害
     * - 在每次造成伤害时播放闪电效果
     * @param p 玩家
     * @param m 目标敌人（此卡作用于所有敌人）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 遍历当前房间的所有敌人
        for (AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters) {
            // 如果怪物没有死亡且已受到虚弱状态
            if (!monster.isDeadOrEscaped() && monster.hasPower(WeakPower.POWER_ID)) {
                // 为该敌人播放闪电特效
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(monster.drawX, monster.drawY), 0.05f));
                // 对虚弱的敌人造成伤害
                AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.BLUNT_LIGHT, true));
            }
        }
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 Thundercloud 实例
     */
    public AbstractCard makeCopy() {
        return new Thundercloud();
    }

    /**
     * unveil 方法：抽到卡牌时触发
     * - 给一个随机敌人施加虚弱效果
     */
    @Override
    public void unveil() {
        // 随机选择一个敌人
        AbstractMonster monster = AbstractDungeon.getRandomMonster();
        // 对该敌人施加虚弱状态
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, AbstractDungeon.player, new WeakPower(monster, magicNumber, false), magicNumber));
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 升级后增加伤害
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();        // 升级卡牌名称，通常在名称后加“+”符号
            upgradeDamage(UPGRADED_BONUS);  // 升级伤害，增加额外的伤害
        }
    }
}
