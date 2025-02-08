package witchmod.cards;

import com.badlogic.gdx.graphics.Color;  // 引入颜色类，用于效果显示
import com.megacrit.cardcrawl.actions.animations.VFXAction;  // 引入VFX动画效果类
import com.megacrit.cardcrawl.actions.common.DrawCardAction;  // 引入抽卡行动类
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入卡牌类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;  // 引入怪物类
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;  // 引入垂直光环效果

/**
 * Wicked Thoughts 卡牌：保持卡牌，针对手牌中的每张诅咒卡，抽取等量的卡牌。
 */
public class WickedThoughts extends AbstractWitchCard {
    // 卡牌的基本信息
    public static final String ID = "WickedThoughts";  // 唯一ID
    public static final String NAME = "Wicked Thoughts";  // 卡牌名称
    public static final String IMG = "cards/wickedthoughts.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "Retain. Draw !M! card for each curse in your hand.";  // 描述：保持此卡。针对手牌中的每张诅咒卡，抽取 !M! 张卡牌。
    public static final String DESCRIPTION_UPGRADED = "Retain. NL Draw !M! cards for each curse in your hand.";  // 升级后的描述：保持此卡。针对手牌中的每张诅咒卡，抽取 !M! 张卡牌（升级后效果）。

    private static final CardRarity RARITY = CardRarity.COMMON;  // 稀有度：普通（COMMON）
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己（SELF）
    private static final CardType TYPE = CardType.SKILL;  // 卡牌类型：技能（SKILL）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int COST = 0;  // 能量消耗：0 点
    private static final int POWER = 1;  // 每张诅咒卡所增加的抽卡数
    private static final int UPGRADED_BONUS = 1;  // 升级后的额外抽卡数

    /**
     * 构造函数：初始化卡牌属性
     */
    public WickedThoughts() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWER;  // 设置基础魔法数值（每张诅咒卡增加的抽卡数）
        this.retain = true;  // 设置保持卡牌（玩家下一回合仍能持有该卡）
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 检查玩家手牌中的诅咒卡数量
     * - 对每张诅咒卡，抽取 !M! 张卡牌（`magicNumber` 表示每张诅咒卡增加的抽卡数量）
     * @param p 玩家
     * @param m 目标怪物（此卡不直接攻击怪物，因此 m 不使用）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = 0;  // 计数，记录手牌中的诅咒卡数量
        // 遍历玩家手牌
        for (AbstractCard c : p.hand.group) {
            // 如果卡牌是诅咒卡
            if (c.type == CardType.CURSE) {
                effect++;  // 增加计数
                if (upgraded) {
                    effect++;  // 如果卡牌已升级，增加额外的效果
                }
            }
        }
        if (effect > 0) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(m, effect));  // 执行抽取卡牌的动作
            AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new VerticalAuraEffect(Color.BLACK, p.hb.cX, p.hb.cY), 0.2f));  // 执行视觉效果（黑色垂直光环）
        }
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 WickedThoughts 实例
     */
    public AbstractCard makeCopy() {
        return new WickedThoughts();
    }

    /**
     * atTurnStart 方法：回合开始时调用
     * - 设置卡牌保持效果
     */
    @Override
    public void atTurnStart() {
        super.atTurnStart();
        retain = true;  // 保持该卡牌
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 升级后抽取的卡牌数量增加
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();  // 升级卡牌名称，通常在名称后加“+”符号
            upgradeMagicNumber(UPGRADED_BONUS);  // 升级后增加抽卡数量
            upgradeDescription();  // 升级描述
            retain = true;  // 保持该卡牌
        }
    }
}
