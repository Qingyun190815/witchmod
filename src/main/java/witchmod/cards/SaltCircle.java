package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction; // 用于增加护甲（Block）
import com.megacrit.cardcrawl.cards.AbstractCard; // 基础卡牌类，所有卡牌均继承自此类
import com.megacrit.cardcrawl.characters.AbstractPlayer; // 玩家角色的抽象类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon; // 游戏主要逻辑管理类，处理动作队列、卡牌等
import com.megacrit.cardcrawl.monsters.AbstractMonster; // 敌人的抽象类

/**
 * SaltCircle（盐之结界）是一张技能卡：
 * - 具有“保留”效果（Retain）：未使用时会保留在手牌中，不会在回合结束时被丢弃
 * - 提供护甲值（Block）
 */
public class SaltCircle extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "SaltCircle";          // 唯一ID，用于识别卡牌
    public static final String NAME = "Salt Circle";       // 卡牌名称
    public static final String IMG = "cards/saltcircle.png"; // 卡牌图片路径
    public static final String DESCRIPTION = "Retain. NL Gain !B! Block.";
    // 描述：保留。获得 !B! 护甲值。NL 表示换行。

    private static final CardRarity RARITY = CardRarity.COMMON; // 稀有度：普通
    private static final CardTarget TARGET = CardTarget.SELF;   // 目标：自己
    private static final CardType TYPE = CardType.SKILL;        // 卡牌类型：技能卡

    private static final int POOL = 1; // 冗余参数，未被实际逻辑使用

    private static final int COST = 1;            // 能量消耗：1
    private static final int POWER = 7;           // 初始护甲值（Block）：7
    private static final int UPGRADE_BONUS = 3;   // 升级后的护甲加成：+3

    /**
     * 构造函数：初始化卡牌的基本属性
     */
    public SaltCircle() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;   // 设置基础护甲值
        this.retain = true;       // 启用“保留”效果：未使用时不会被弃牌
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * @param p 玩家
     * @param m 目标敌人（此卡不与敌人互动，参数未使用）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 为玩家增加等于当前 block 值的护甲
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 SaltCircle 实例
     */
    public AbstractCard makeCopy() {
        return new SaltCircle();
    }

    /**
     * atTurnStart 方法：每个回合开始时调用
     * 确保卡牌始终具有“保留”效果
     */
    @Override
    public void atTurnStart() {
        retain = true; // 重置保留状态，确保升级或效果不会意外禁用此功能
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 提升护甲值（+3）
     * - 升级卡牌名称以显示视觉差异
     */
    public void upgrade() {
        if (!upgraded) {               // 检查是否已经升级，避免重复升级
            upgradeName();             // 升级卡牌名称，通常会在名称后加“+”符号
            upgradeBlock(UPGRADE_BONUS); // 增加护甲值
        }
    }
}
