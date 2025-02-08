package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction; // 用于应用增益或减益效果
import com.megacrit.cardcrawl.cards.AbstractCard; // 所有卡牌的基础类
import com.megacrit.cardcrawl.characters.AbstractPlayer; // 玩家角色抽象类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon; // 游戏主逻辑管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster; // 敌人抽象类
import witchmod.powers.SchadenfreudePower; // 自定义的 Schadenfreude 力量类

/**
 * Schadenfreude（幸灾乐祸）是一张力量卡（Power Card）：
 * - 效果：每当玩家对敌人施加一个负面效果（Debuff）时，获得一定量的护甲（Block）。
 */
public class Schadenfreude extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "Schadenfreude";               // 唯一ID，用于卡牌识别
    public static final String NAME = "Schadenfreude";             // 卡牌名称
    public static final String IMG = "cards/schadenfreude.png";    // 卡牌图片路径
    public static final String DESCRIPTION = "Whenever you apply a Debuff to an enemy gain !M! block.";
    // 描述：每当你对敌人施加一个负面效果时，获得 !M! 点护甲（!M! 代表 magicNumber 的值）。

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 稀有度：非普通（Uncommon）
    private static final CardTarget TARGET = CardTarget.SELF;      // 目标：自己
    private static final CardType TYPE = CardType.POWER;           // 卡牌类型：力量卡（Power）

    private static final int POOL = 1; // 冗余参数，未被实际逻辑使用

    private static final int COST = 1;               // 能量消耗：1 点
    private static final int POWER = 2;              // 初始效果强度：每次施加负面效果时获得 2 点护甲
    private static final int UPGRADED_BONUS = 1;     // 升级加成：+1 护甲（升级后每次获得 3 点护甲）

    /**
     * 构造函数：初始化卡牌属性
     */
    public Schadenfreude() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWER; // 设置基础效果值
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * @param p 玩家
     * @param m 目标敌人（此卡不与敌人直接互动，参数未使用）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 将 SchadenfreudePower 应用到玩家身上，效果值为 magicNumber
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new SchadenfreudePower(p, magicNumber), magicNumber)
        );
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 Schadenfreude 实例
     */
    public AbstractCard makeCopy() {
        return new Schadenfreude();
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 增强卡牌效果：提升每次获得的护甲值
     * - 升级卡牌名称以展示视觉差异
     */
    public void upgrade() {
        if (!upgraded) {                   // 检查是否已升级，防止重复升级
            upgradeName();                 // 升级卡牌名称，通常在名称后加“+”符号
            upgradeMagicNumber(UPGRADED_BONUS); // 提升 magicNumber，增加获得的护甲量
        }
    }
}
