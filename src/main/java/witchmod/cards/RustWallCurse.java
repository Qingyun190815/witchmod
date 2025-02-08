package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.SetDontTriggerAction; // 用于设置卡牌的触发状态，防止无限循环触发
import com.megacrit.cardcrawl.cards.AbstractCard; // 基础卡牌类，所有卡牌都继承自此类
import com.megacrit.cardcrawl.cards.CardQueueItem; // 表示卡牌在行动队列中的项
import com.megacrit.cardcrawl.characters.AbstractPlayer; // 玩家角色的抽象类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon; // 游戏的主要逻辑控制类，处理行动、效果等
import com.megacrit.cardcrawl.monsters.AbstractMonster; // 敌人的抽象类
import com.megacrit.cardcrawl.vfx.GenericSmokeEffect; // 烟雾视觉效果
import witchmod.actions.ReduceBlockAction; // 自定义动作：减少护甲值（Block）

/**
 * RustWallCurse（锈蚀诅咒）是一张诅咒卡：
 * - 不可打出（Unplayable）
 * - 回合结束时强制触发，移除玩家当前护甲值的一半
 * - 具有虚无（Ethereal）效果，若本回合未使用则会被消耗（Exhaust）
 */
public class RustWallCurse extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "RustWallCurse";          // 唯一ID
    public static final String NAME = "Curse of Rust";        // 卡牌名称
    public static final String IMG = "cards/rustwallcurse.png"; // 卡牌图片路径
    public static final String DESCRIPTION = "Unplayable. At the end of your turn lose half of your Block. NL Ethereal.";
    // 描述：无法使用，回合结束时失去一半护甲值，具有虚无效果

    private static final CardRarity RARITY = CardRarity.SPECIAL; // 稀有度：特殊（通常为诅咒或遗物卡）
    private static final CardTarget TARGET = CardTarget.NONE;    // 目标：无（因为无法打出）
    private static final CardType TYPE = CardType.CURSE;         // 卡牌类型：诅咒

    private static final int POOL = 2; // 冗余参数，未在实际逻辑中使用
    private static final int COST = -2; // 卡牌费用，-2 代表不可打出（Unplayable）

    private static final int POWER = 10; // 冗余参数，未直接参与卡牌效果，可能为开发测试遗留

    /**
     * 构造函数：初始化卡牌的基本属性
     */
    public RustWallCurse() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWER; // 冗余参数设置，未被实际逻辑调用
        this.exhaust = true; // 虚无（Ethereal）：未使用即消耗
    }

    /**
     * use 方法：当卡牌被"强制使用"时触发（如与蓝蜡烛互动或自动触发）
     * @param p 玩家
     * @param m 目标敌人（此处未使用）
     */
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 与“蓝蜡烛”遗物交互：允许玩家手动移除诅咒卡
        if (!dontTriggerOnUseCard && p.hasRelic("Blue Candle")) {
            useBlueCandle(p); // 处理蓝蜡烛效果（消耗生命值移除诅咒）
        } else {
            // 计算要移除的护甲值：当前护甲值的一半
            int blockToConsume = Math.floorDiv(p.currentBlock, 2);

            // 生成烟雾视觉效果，模拟“锈蚀”效果
            for (int i = 0; i < 10; i++) {
                float dx = (float) (Math.random() * 30 - 15); // 随机X偏移
                float dy = (float) (Math.random() * 30 - 15); // 随机Y偏移
                AbstractDungeon.effectsQueue.add(new GenericSmokeEffect(p.hb.cX + dx, p.hb.cY + dy));
            }

            // 执行减少护甲的动作
            AbstractDungeon.actionManager.addToTop(new ReduceBlockAction(p, p, blockToConsume));

            // 重置卡牌的“不触发”状态，确保下一轮能再次生效
            AbstractDungeon.actionManager.addToBottom(new SetDontTriggerAction(this, false));
        }
    }

    /**
     * triggerWhenDrawn 方法：当抽到此卡牌时触发
     * 主要用于重置“不触发”标志，避免意外的循环触发
     */
    @Override
    public void triggerWhenDrawn() {
        AbstractDungeon.actionManager.addToBottom(new SetDontTriggerAction(this, false));
    }

    /**
     * triggerOnEndOfTurnForPlayingCard 方法：
     * 在回合结束时强制将此卡牌加入行动队列，模拟自动触发效果
     */
    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        dontTriggerOnUseCard = true; // 防止再次触发蓝蜡烛的逻辑
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true)); // 加入行动队列
    }

    /**
     * 创建卡牌的副本
     * @return 新的 RustWallCurse 实例
     */
    public AbstractCard makeCopy() {
        return new RustWallCurse();
    }

    /**
     * 升级方法：诅咒卡通常不可升级，因此此方法为空
     */
    public void upgrade() {
        // 诅咒卡不具备升级逻辑
    }
}
