package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;        // 获得格挡（Block）的动作
import com.megacrit.cardcrawl.cards.AbstractCard;                    // 卡牌的基础抽象类
import com.megacrit.cardcrawl.characters.AbstractPlayer;            // 玩家抽象类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;             // 游戏主逻辑控制类
import com.megacrit.cardcrawl.monsters.AbstractMonster;             // 敌人抽象类

/**
 * Soul Barrier（灵魂屏障）是一张技能卡（Skill Card）：
 * - 效果：抽到时获得格挡，使用后再次获得格挡，使用后消耗（Exhaust）。
 * - 具有双重防御机制，适合用于快速应对突发伤害。
 */
public class SoulBarrier extends AbstractWitchUnveilCard {
    // 卡牌的基础信息
    public static final String ID = "SoulBarrier";                   // 唯一ID
    public static final String NAME = "Soul Barrier";               // 卡牌名称
    public static final String IMG = "cards/soulbarrier.png";       // 卡牌图片路径
    public static final String DESCRIPTION = "When drawn gain !M! Block. NL Gain !B! Block. NL Exhaust.";
    // 描述：抽到时获得 !M! 点格挡，使用后再获得 !B! 点格挡，使用后消耗。

    private static final CardRarity RARITY = CardRarity.COMMON;     // 稀有度：普通（Common）
    private static final CardTarget TARGET = CardTarget.SELF;       // 目标：自己
    private static final CardType TYPE = CardType.SKILL;            // 卡牌类型：技能卡（Skill）

    private static final int POOL = 1;          // 冗余参数，未被实际使用，保留以便拓展
    private static final int COST = 0;          // 能量消耗：0 点（免费卡牌）
    private static final int POWER = 6;         // 使用时获得的基础格挡值：6 点

    private static final int UPGRADE_BONUS = 2;         // 升级后格挡值增加：2 点（升级后为 8 点）
    private static final int MAGIC = 3;                  // 抽到时获得的基础格挡值：3 点
    private static final int MAGIC_UPGRADE_BONUS = 1;    // 升级后抽牌时格挡值增加：1 点（升级后为 4 点）

    /**
     * 构造函数：初始化卡牌属性
     */
    public SoulBarrier() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;                         // 设置使用时获得的基础格挡
        this.baseMagicNumber = this.magicNumber = MAGIC; // 设置抽到卡牌时获得的基础格挡
        this.exhaust = true;                           // 卡牌使用后会被消耗（Exhaust）
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 为玩家提供 !B! 点格挡（block）
     * @param p 玩家
     * @param m 目标敌人（此卡不作用于敌人，参数保留以兼容父类）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 将获得格挡的动作添加到动作队列
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 SoulBarrier 实例
     */
    public AbstractCard makeCopy() {
        return new SoulBarrier();
    }

    /**
     * unveil 方法：当这张卡牌被抽到时触发的特殊效果
     * - 立即为玩家提供 !M! 点格挡（block）
     */
    @Override
    public void unveil() {
        // 将获得格挡的动作添加到动作队列
        AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, magicNumber)
        );
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 增加使用时和抽到时获得的格挡值
     * - 升级卡牌名称以展示视觉差异
     */
    public void upgrade() {
        if (!upgraded) {                  // 确保卡牌只被升级一次
            upgradeName();                // 升级卡牌名称，通常在名称后加“+”符号
            upgradeBlock(UPGRADE_BONUS);  // 增加使用时获得的格挡值
            upgradeMagicNumber(MAGIC_UPGRADE_BONUS); // 增加抽牌时获得的格挡值
        }
    }
}
