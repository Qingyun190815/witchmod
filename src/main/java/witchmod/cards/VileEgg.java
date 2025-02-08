package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.HealAction;  // 引入治疗效果
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;  // 引入临时卡牌生成效果
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入卡牌类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;  // 引入怪物类
import witchmod.powers.SummonFamiliarPower;  // 引入召唤熟悉物卡牌生成的功能

/**
 * VileEgg 卡牌：治疗玩家并随机生成一个熟悉物卡牌，该卡牌本回合费用为 0，且在使用后会被弃掉。
 */
public class VileEgg extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "VileEgg";  // 唯一ID
    public static final String NAME = "Vile Egg";  // 卡牌名称
    public static final String IMG = "cards/vileegg.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "Heal !M! HP. NL Add a random Familiar to your hand. It costs 0 this turn. NL Exhaust";  // 描述：治疗 !M! 生命值并将一个随机的熟悉物卡牌加入手牌，费用为 0，本回合有效且弃掉。
    public static final String DESCRIPTION_UPGRADED = "Heal !M! HP. NL Add a random upgraded Familiar to your hand. It costs 0 this turn. Exhaust";  // 升级描述：将一个升级后的随机熟悉物卡牌加入手牌，费用为 0，本回合有效且弃掉。

    private static final CardRarity RARITY = CardRarity.RARE;  // 稀有度：稀有（RARE）
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己（SELF）
    private static final CardType TYPE = CardType.SKILL;  // 卡牌类型：技能（SKILL）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int COST = 0;  // 能量消耗：0 点
    private static final int POWER = 3;  // 初始治疗效果
    private static final int UPGRADE_BONUS = 2;  // 升级后的治疗效果加成

    /**
     * 构造函数：初始化卡牌属性
     */
    public VileEgg() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER;  // 设置基础魔法数值（治疗效果）
        this.exhaust = true;  // 设置卡牌使用后会被弃掉
        this.tags.add(CardTags.HEALING);  // 添加治疗标签
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 治疗玩家生命值
     * - 随机选择一个熟悉物卡牌加入手牌，并设置为 0 点费用
     * @param p 玩家
     * @param m 目标怪物（此卡不涉及直接攻击敌人，因此 m 不使用）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, magicNumber));  // 给玩家治疗生命值
        AbstractCard toCreate = SummonFamiliarPower.getRandomFamiliarCard();  // 随机生成一个熟悉物卡牌
        if (upgraded) {  // 如果卡牌已经升级
            toCreate.upgrade();  // 升级熟悉物卡牌
        }
        toCreate.costForTurn = 0;  // 设置熟悉物卡牌本回合费用为 0
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(toCreate, 1));  // 将生成的卡牌加入手牌并将其作为临时卡牌处理
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 VileEgg 实例
     */
    public AbstractCard makeCopy() {
        return new VileEgg();
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 升级后治疗效果增强
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();  // 升级卡牌名称，通常在名称后加“+”符号
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级后增加治疗效果
            upgradeDescription();  // 升级卡牌描述，通常是增加描述中的数字
        }
    }
}
