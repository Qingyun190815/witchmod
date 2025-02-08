package witchmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;         // 引入卡牌的基础类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.monsters.AbstractMonster;   // 引入敌人类
import com.megacrit.cardcrawl.potions.AbstractPotion;     // 引入药水类
import com.megacrit.cardcrawl.potions.FairyPotion;        // 引入仙女药水类
import com.megacrit.cardcrawl.potions.SmokeBomb;          // 引入烟雾弹类

/**
 * Strange Brew（奇异饮品）是一张技能卡（Skill Card）：
 * - 效果：对自己施加所有可饮用药水的效果，但不消耗药水。卡牌使用后会被消耗（Exhaust）。
 * - 适用于在没有消耗药水的情况下临时获得多个药水的效果。
 */
public class StrangeBrew extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "StrangeBrew";           // 唯一ID
    public static final String NAME = "Strange Brew";        // 卡牌名称
    public static final String IMG = "cards/strangebrew.png"; // 卡牌图片路径
    public static final String DESCRIPTION = "Apply on yourself the effect of all your drinkable potions, without consuming them. Exhaust.";
    // 描述：对自己施加所有可饮用药水的效果，但不消耗药水。使用后消耗。

    private static final CardRarity RARITY = CardRarity.RARE; // 稀有度：稀有（Rare）
    private static final CardTarget TARGET = CardTarget.SELF; // 目标：自己
    private static final CardType TYPE = CardType.SKILL;      // 卡牌类型：技能卡（Skill）

    private static final int POOL = 1;          // 冗余参数，未被实际使用，保留以便拓展
    private static final int COST = 3;          // 能量消耗：3 点
    private static final int UPGRADED_COST = 2; // 升级后能量消耗：2 点

    /**
     * 构造函数：初始化卡牌属性
     */
    public StrangeBrew() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.exhaust = true;  // 使用后卡牌将被消耗
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 遍历玩家的所有药水，施加其效果（不消耗药水）
     * @param p 玩家
     * @param m 目标敌人（此卡不作用于敌人，参数保留以兼容父类）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 遍历玩家的所有药水
        for (AbstractPotion potion : p.potions) {
            // 如果药水未被投掷且未被限制
            if (potion.isThrown == false && !(isRestricted(potion))) {
                potion.use(p);  // 施加药水效果
            }
        }
    }

    /**
     * canUse 方法：判断玩家是否可以使用此卡牌
     * - 遍历玩家的所有药水，检查是否有可以使用的药水
     * @param p 玩家
     * @param m 目标敌人（此卡不作用于敌人，参数保留以兼容父类）
     * @return 如果玩家有可以使用的药水，则返回 true；否则返回 false
     */
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        int usable = 0;  // 可用药水的计数
        for (AbstractPotion potion : p.potions) {
            // 如果药水未被投掷且未被限制
            if (potion.isThrown == false && !(isRestricted(potion))) {
                usable++;  // 计数可用药水
            }
        }
        if (usable == 0) {
            cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0]; // 没有可用药水时的提示信息
            return false;  // 返回不可使用
        }
        return true;  // 如果有可用药水，则返回可使用
    }

    /**
     * isRestricted 方法：检查药水是否被限制
     * - 目前限制的药水包括：烟雾弹和仙女药水
     * @param potion 药水实例
     * @return 如果药水被限制，则返回 true；否则返回 false
     */
    protected boolean isRestricted(AbstractPotion potion) {
        return potion.ID.equals(SmokeBomb.POTION_ID)  // 限制烟雾弹
                || potion.ID.equals(FairyPotion.POTION_ID);  // 限制仙女药水
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 StrangeBrew 实例
     */
    public AbstractCard makeCopy() {
        return new StrangeBrew();
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 降低能量消耗至 2 点
     * - 升级卡牌名称以展示视觉差异
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();                // 升级卡牌名称，通常在名称后加“+”符号
            upgradeBaseCost(UPGRADED_COST);  // 降低能量消耗
        }
    }
}
