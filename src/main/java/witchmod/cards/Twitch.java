package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;  // 引入应用力量的动作
import com.megacrit.cardcrawl.actions.common.GainBlockAction;  // 引入获得护甲的动作
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入卡牌的基础类
import com.megacrit.cardcrawl.characters.AbstractPlayer;  // 引入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入游戏中的地下城管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster;  // 引入怪物类
import com.megacrit.cardcrawl.powers.DexterityPower;  // 引入敏捷属性的力量类
import witchmod.powers.LoseDexterityPower;  // 引入失去敏捷的力量类

/**
 * Twitch 卡牌：每当抽到此卡时，获得一定的敏捷和护甲，并且在回合结束时失去一定的敏捷。
 */
public class Twitch extends AbstractWitchUnveilCard {
    // 卡牌的基础信息
    public static final String ID = "Twitch";  // 唯一ID
    public static final String NAME = "Twitch";  // 卡牌名称
    public static final String IMG = "cards/twitch.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "When drawn gain !M! Dexterity for 1 turn. NL Gain !B! block.";  // 描述：每当抽到此卡时，获得一定的敏捷和护甲。

    private static final CardRarity RARITY = CardRarity.RARE;  // 稀有度：稀有（RARE）
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己（SELF）
    private static final CardType TYPE = CardType.SKILL;  // 卡牌类型：技能（SKILL）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int COST = 2;  // 能量消耗：2 点
    private static final int POWER = 14;  // 初始护甲值
    private static final int UPGRADE_BONUS = 3;  // 升级后的护甲加成
    private static final int MAGIC = 2;  // 初始敏捷值
    private static final int MAGIC_UPGRADE_BONUS = 1;  // 升级后的敏捷加成

    /**
     * 构造函数：初始化卡牌属性
     */
    public Twitch() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置基础护甲值
        this.baseMagicNumber = this.magicNumber = MAGIC;  // 设置基础魔法数值（敏捷）
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * - 给玩家增加护甲
     * @param p 玩家
     * @param m 目标敌人（此卡不涉及直接攻击敌人，因此 m 不使用）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给玩家增加护甲
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 Twitch 实例
     */
    public AbstractCard makeCopy() {
        return new Twitch();
    }

    /**
     * unveil 方法：每当此卡被抽到时触发
     * - 玩家获得一定的敏捷（Dexterity），并在结束时失去相应的敏捷
     */
    @Override
    public void unveil() {
        // 给玩家应用敏捷提升效果
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, magicNumber), magicNumber));
        // 给玩家应用失去敏捷的效果，在本回合结束时失去敏捷
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseDexterityPower(AbstractDungeon.player, magicNumber), magicNumber));
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 升级后增加护甲值和敏捷值
     */
    public void upgrade() {
        if (!upgraded) {  // 确保卡牌只被升级一次
            upgradeName();  // 升级卡牌名称，通常在名称后加“+”符号
            upgradeBlock(UPGRADE_BONUS);  // 升级后增加护甲值
            upgradeMagicNumber(MAGIC_UPGRADE_BONUS);  // 升级后增加敏捷值
        }
    }
}
