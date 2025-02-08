package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;  // 引入应用力量的动作
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入卡牌的基础类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入游戏中的地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;  // 引入怪物类
import witchmod.powers.TrollsBloodPower;  // 引入 TrollsBloodPower 类，该类定义了 Troll's Blood 的效果

/**
 * Troll's Blood 卡牌：
 * - 使玩家在每回合结束时，如果本回合内至少使用过一张攻击卡牌，便恢复一定的生命值。
 */
public class TrollsBlood extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "TrollsBlood";  // 唯一ID
    public static final String NAME = "Troll's Blood";  // 卡牌名称
    public static final String IMG = "cards/trollsblood.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "Recover !M! health at the end of each turn in which you played at least an attack.";  // 描述：每回合结束时，如果本回合至少使用过一张攻击卡牌，则恢复一定的生命值。

    private static final CardRarity RARITY = CardRarity.RARE;  // 稀有度：稀有（RARE）
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己（SELF）
    private static final CardType TYPE = CardType.POWER;  // 卡牌类型：力量（POWER）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int POWER = 1;  // 初始恢复生命值的强度
    private static final int COST = 1;  // 能量消耗：1 点
    private static final int UPGRADED_COST = 0;  // 升级后的能量消耗：0 点

    /**
     * 构造函数：初始化卡牌属性
     */
    public TrollsBlood() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER;  // 设置魔法数值，控制每回合恢复的生命值
        this.tags.add(CardTags.HEALING);  // 为卡牌添加治疗标签
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 给玩家添加 TrollsBloodPower，使其能够在每回合结束时恢复生命值
     * @param p 玩家
     * @param m 目标敌人（此卡不涉及敌人，因此 m 不使用）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给玩家应用 TrollsBloodPower，使其在每回合结束时恢复生命值
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new TrollsBloodPower(p, magicNumber), magicNumber));
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 TrollsBlood 实例
     */
    public AbstractCard makeCopy() {
        return new TrollsBlood();
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 升级后将能量消耗减少为 0
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();  // 升级卡牌名称，通常在名称后加“+”符号
            upgradeBaseCost(UPGRADED_COST);  // 升级后将卡牌的能量消耗设置为 0
        }
    }
}
