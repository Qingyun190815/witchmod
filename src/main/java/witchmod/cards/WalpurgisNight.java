package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;  // 引入抽卡行动
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入卡牌类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;  // 引入怪物类
import witchmod.actions.WalpurgisNightAction;  // 引入自定义的 WalpurgisNight 行动

/**
 * Walpurgis Night 卡牌：将所有已消耗的非虚无的攻击和技能卡重新洗入抽牌堆，然后抽取其中的 !M! 张卡牌。并将该卡牌弃掉。
 */
public class WalpurgisNight extends AbstractWitchCard {
    // 卡牌的基本信息
    public static final String ID = "WalpurgisNight";  // 唯一ID
    public static final String NAME = "Walpurgis Night";  // 卡牌名称
    public static final String IMG = "cards/walpurgisnight.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "Shuffle ALL Exhausted non-Etheral Attacks and Skills into your draw pile, then draw !M! of those cards. Exhaust";  // 描述：将所有已消耗的非虚无攻击和技能卡加入抽牌堆，然后抽取 !M! 张这些卡牌。弃掉该卡牌。

    private static final CardRarity RARITY = CardRarity.RARE;  // 稀有度：稀有（RARE）
    private static final CardTarget TARGET = CardTarget.NONE;  // 目标：无目标（NONE）
    private static final CardType TYPE = CardType.SKILL;  // 卡牌类型：技能（SKILL）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int COST = 0;  // 能量消耗：0 点
    private static final int POWER = 1;  // 初始抽取卡牌数量
    private static final int UPGRADE_BONUS = 2;  // 升级后抽取卡牌数量加成

    /**
     * 构造函数：初始化卡牌属性
     */
    public WalpurgisNight() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWER;  // 设置基础魔法数值（抽取卡牌数量）
        this.exhaust = true;  // 设置卡牌使用后会被弃掉
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 将所有已消耗的非虚无攻击和技能卡洗入抽牌堆
     * - 抽取其中的 !M! 张卡牌
     * @param p 玩家
     * @param m 目标怪物（此卡不涉及直接攻击敌人，因此 m 不使用）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new WalpurgisNightAction(magicNumber));  // 执行自定义行动（洗入抽牌堆）
        int cardsToDraw = 0;  // 计数，记录需要抽取的卡牌数
        // 遍历玩家的消耗堆
        for (AbstractCard c : p.exhaustPile.group) {
            // 筛选出非虚无且为攻击或技能的卡牌
            if ((c.type == CardType.ATTACK || c.type == CardType.SKILL) && !c.isEthereal && !c.cardID.equals(this.cardID)) {
                cardsToDraw++;  // 增加计数
                if (cardsToDraw >= magicNumber) {  // 如果抽取的卡牌数达到最大限制
                    cardsToDraw = magicNumber;  // 限制为最大抽卡数
                    break;  // 跳出循环
                }
            }
        }
        if (cardsToDraw > 0) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, cardsToDraw));  // 执行抽取卡牌的动作
        }
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 WalpurgisNight 实例
     */
    public AbstractCard makeCopy() {
        return new WalpurgisNight();
    }

    /**
     * canUse 方法：判断是否可以使用此卡
     * - 检查玩家的消耗堆中是否存在可用的卡牌
     * @param p 玩家
     * @param m 目标怪物
     * @return 是否可以使用此卡
     */
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // 遍历消耗堆中的卡牌
        for (AbstractCard c : p.exhaustPile.group) {
            // 判断卡牌是否符合条件（攻击、技能卡且非虚无卡牌）
            if ((c.type == CardType.ATTACK || c.type == CardType.SKILL) && !c.isEthereal && !c.cardID.equals(this.cardID)) {
                return super.canUse(p, m);  // 如果存在符合条件的卡牌，允许使用
            }
        }
        // 如果没有符合条件的卡牌，返回不可用的状态
        cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];  // 设置无法使用时的提示信息
        return false;
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 升级后抽卡数量增加
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();  // 升级卡牌名称，通常在名称后加“+”符号
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级后增加抽取卡牌数量
        }
    }
}
