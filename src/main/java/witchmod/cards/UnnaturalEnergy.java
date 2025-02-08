package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;  // 引入获得能量的动作
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入卡牌的基础类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入游戏中的地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;  // 引入怪物类

/**
 * UnnaturalEnergy 卡牌：允许玩家获得能量，并具有重复效果（每回合使用一次）。
 */
public class UnnaturalEnergy extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "UnnaturalEnergy";  // 唯一ID
    public static final String NAME = "Unnatural Energy";  // 卡牌名称
    public static final String IMG = "cards/unnaturalenergy.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "Gain [B] . NL Recurrent.";  // 描述：获得 1 点能量，重复使用。
    public static final String DESCRIPTION_UPGRADED = "Gain [B] [B] . NL Recurrent.";  // 升级描述：获得 2 点能量，重复使用。

    private static final CardRarity RARITY = CardRarity.RARE;  // 稀有度：稀有（RARE）
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己（SELF）
    private static final CardType TYPE = CardType.SKILL;  // 卡牌类型：技能（SKILL）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int COST = 0;  // 能量消耗：0 点
    private static final int POWER = 1;  // 初始能量值
    private static final int UPGRADE_BONUS = 1;  // 升级后的能量加成

    /**
     * 构造函数：初始化卡牌属性
     */
    public UnnaturalEnergy() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER;  // 设置基础魔法数值（能量）
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 给玩家增加能量
     * @param p 玩家
     * @param m 目标敌人（此卡不涉及直接攻击敌人，因此 m 不使用）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        reshuffleOnUse = true;  // 使用卡牌后将其重新洗入牌库
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(magicNumber));  // 给玩家增加指定数量的能量
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 UnnaturalEnergy 实例
     */
    public AbstractCard makeCopy() {
        return new UnnaturalEnergy();
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 升级后增加能量值
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();  // 升级卡牌名称，通常在名称后加“+”符号
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级后增加能量值
            upgradeDescription();  // 升级卡牌描述，通常是增加描述中的数字
        }
    }
}
