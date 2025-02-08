package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;  // 引入应用力量的动作
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;  // 引入移除特定力量的动作
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入卡牌的基础类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入游戏中的地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;  // 引入怪物类
import witchmod.cards.familiar.FamiliarCardEnum;  // 引入随从卡的枚举
import witchmod.cards.familiar.RavenFamiliar;  // 引入乌鸦随从的卡牌类
import witchmod.powers.SummonFamiliarPower;  // 引入召唤随从力量的类

/**
 * SummonRavenFamiliar（召唤乌鸦随从）卡牌：
 * - 在回合开始时添加一只乌鸦到手牌，移除其他随从力量。
 * - 升级后，乌鸦是升级版，其他效果相同。
 */
public class SummonRavenFamiliar extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "SummonRavenFamiliar";  // 唯一ID
    public static final String NAME = "Raven Familiar";     // 卡牌名称
    public static final String IMG = "cards/summonraven.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "At the start of your turn, add a Raven to your hand. NL Removes other Familiar powers.";  // 描述：在回合开始时，将一只乌鸦随从加入手牌。移除其他随从力量。
    public static final String DESCRIPTION_UPGRADED = "At the start of your turn, add an upgraded Raven to your hand. Removes other Familiar powers.";  // 升级描述：在回合开始时，将一只升级版乌鸦随从加入手牌。移除其他随从力量。

    private static final CardRarity RARITY = CardRarity.RARE;  // 稀有度：稀有（RARE）
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.POWER;  // 卡牌类型：力量（POWER）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int COST = 0;  // 能量消耗：0 点

    /**
     * 构造函数：初始化卡牌属性
     */
    public SummonRavenFamiliar() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        cardPreviewTooltip = new RavenFamiliar();  // 设置卡牌预览，显示乌鸦随从的效果
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 移除玩家的其他随从力量
     * - 为玩家应用召唤乌鸦随从的力量
     * @param p 玩家
     * @param m 目标敌人（此卡不使用目标）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 如果玩家已经拥有完整的召唤随从力量，则移除该力量
        if (p.hasPower(SummonFamiliarPower.POWER_ID_FULL)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SummonFamiliarPower.POWER_ID_FULL));
        }
        // 应用新的召唤乌鸦随从力量（根据是否升级决定效果）
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SummonFamiliarPower(p, FamiliarCardEnum.RAVEN, upgraded)));
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 SummonRavenFamiliar 实例
     */
    public AbstractCard makeCopy() {
        return new SummonRavenFamiliar();
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 升级卡牌的名称、描述以及卡牌预览效果
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();        // 升级卡牌名称，通常在名称后加“+”符号
            upgradeDescription();  // 升级卡牌描述
            cardPreviewTooltip.upgrade();  // 升级卡牌预览效果（乌鸦随从）
        }
    }
}
