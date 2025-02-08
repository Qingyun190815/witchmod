package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;    // 引入游戏动作的抽象类
import com.megacrit.cardcrawl.actions.common.DamageAction;    // 引入造成伤害的动作
import com.megacrit.cardcrawl.cards.AbstractCard;             // 引入卡牌的基础类
import com.megacrit.cardcrawl.cards.DamageInfo;               // 引入伤害信息类
import com.megacrit.cardcrawl.characters.AbstractPlayer;      // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;       // 引入游戏中的地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;       // 引入怪物类

/**
 * Strike_Witch（巫师的攻击）是一个基础的攻击卡：
 * - 效果：造成 !D! 伤害。
 * - 卡牌属于基础卡（BASIC）类型，适用于新手玩家作为起始卡牌。
 */
public class Strike_Witch extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "Strike_Witch";              // 唯一ID
    public static final String NAME = "Strike";                   // 卡牌名称
    public static final String IMG = "cards/strike.png";          // 卡牌图片路径
    public static final String DESCRIPTION = "Deal !D! damage.";  // 描述：造成 !D! 伤害

    private static final CardRarity RARITY = CardRarity.BASIC;    // 稀有度：基础（BASIC）
    private static final CardTarget TARGET = CardTarget.ENEMY;    // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;         // 卡牌类型：攻击卡（ATTACK）

    private static final int POOL = 0;                            // 冗余参数，未被实际使用，保留以便拓展
    private static final int COST = 1;                            // 能量消耗：1 点
    private static final int POWER = 6;                           // 基础伤害：6 点
    private static final int UPGRADE_BONUS = 3;                   // 升级后伤害增加：3 点

    /**
     * 构造函数：初始化卡牌属性
     */
    public Strike_Witch() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置卡牌的基础伤害
        // 为卡牌添加标签，指示它是起始攻击卡和普通攻击卡
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 对目标敌人造成伤害
     * @param p 玩家
     * @param m 目标敌人
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 执行伤害动作，造成伤害，并使用斜切的攻击效果
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 Strike_Witch 实例
     */
    public AbstractCard makeCopy() {
        return new Strike_Witch();
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 提升伤害：将伤害增加 UPGRADE_BONUS（3 点）
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();        // 升级卡牌名称，通常在名称后加“+”符号
            upgradeDamage(UPGRADE_BONUS);  // 升级卡牌伤害
        }
    }
}
