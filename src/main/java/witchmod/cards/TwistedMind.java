package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;  // 引入应用力量的动作
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入卡牌的基础类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入游戏中的地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;  // 引入怪物类
import witchmod.powers.TwistedMindPower;  // 引入 TwistedMindPower 类，该类定义了 Twisted Mind 的效果

/**
 * Twisted Mind 卡牌：
 * - 每当你打出一张费用为 2 或更多的卡牌时，所有敌人失去一定的生命。
 */
public class TwistedMind extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "TwistedMind";  // 唯一ID
    public static final String NAME = "Twisted Mind";  // 卡牌名称
    public static final String IMG = "cards/twistedmind.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "Whenever you play a card that costs 2 or more, ALL enemies lose !M! health.";  // 描述：每当你打出一张费用为 2 或更多的卡牌时，所有敌人失去一定的生命。

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 稀有度：不常见（UNCOMMON）
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己（SELF）
    private static final CardType TYPE = CardType.POWER;  // 卡牌类型：力量（POWER）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int COST = 1;  // 能量消耗：1 点
    private static final int POWER = 3;  // 初始敌人生命损失的强度
    private static final int UPGRADED_BONUS = 2;  // 升级后的额外强度

    /**
     * 构造函数：初始化卡牌属性
     */
    public TwistedMind() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER;  // 设置魔法数值，控制敌人损失的生命值
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 给玩家应用 TwistedMindPower，使其在打出费用为 2 或更多的卡牌时，所有敌人损失生命
     * @param p 玩家
     * @param m 目标敌人（此卡不涉及直接攻击敌人，因此 m 不使用）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给玩家应用 TwistedMindPower，该力量使得在打出费用为 2 或更多的卡牌时，所有敌人都会失去一定的生命
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TwistedMindPower(p, magicNumber), magicNumber));
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 TwistedMind 实例
     */
    public AbstractCard makeCopy() {
        return new TwistedMind();
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 升级后增加魔法数值（即敌人失去的生命值）
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();  // 升级卡牌名称，通常在名称后加“+”符号
            upgradeMagicNumber(UPGRADED_BONUS);  // 升级后增加魔法数值
        }
    }
}
