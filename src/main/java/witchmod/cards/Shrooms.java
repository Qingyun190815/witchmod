package witchmod.cards;

import com.badlogic.gdx.graphics.Color; // 处理颜色，用于视觉特效
import com.megacrit.cardcrawl.actions.animations.VFXAction; // 用于在战斗中显示视觉效果
import com.megacrit.cardcrawl.cards.AbstractCard; // 所有卡牌的基础类
import com.megacrit.cardcrawl.characters.AbstractPlayer; // 玩家角色抽象类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon; // 游戏主逻辑管理类
import com.megacrit.cardcrawl.monsters.AbstractMonster; // 敌人抽象类
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect; // 垂直光环特效
import witchmod.actions.ShroomsAction; // 自定义的 Shrooms 动作类

/**
 * Shrooms（蘑菇）是一张技能卡（Skill Card）：
 * - 效果：抽取 !M! 张卡牌，并将它们的费用随机化（仅限本回合有效）。
 * - 具有特殊的粉色光环视觉效果。
 */
public class Shrooms extends AbstractWitchCard {
    // 卡牌的基础信息
    public static final String ID = "Shrooms";                // 唯一ID，用于卡牌识别
    public static final String NAME = "'Shrooms";             // 卡牌名称（带有单引号的设计，可能是风格化处理）
    public static final String IMG = "cards/shrooms.png";     // 卡牌图片路径
    public static final String DESCRIPTION = "Draw !M! cards. They have random costs for this turn.";
    // 描述：抽取 !M! 张卡牌，且它们的费用在本回合内随机化（!M! 代表 magicNumber 的值）。

    private static final CardRarity RARITY = CardRarity.UNCOMMON; // 稀有度：非普通（Uncommon）
    private static final CardTarget TARGET = CardTarget.SELF;     // 目标：自己
    private static final CardType TYPE = CardType.SKILL;          // 卡牌类型：技能卡（Skill）

    private static final int POOL = 1;        // 冗余参数，未被实际逻辑使用
    private static final int COST = 0;        // 能量消耗：0 点（免费使用）
    private static final int POWER = 3;       // 初始效果强度：抽 3 张牌
    private static final int UPGRADED_BONUS = 1; // 升级加成：额外抽 1 张牌

    /**
     * 构造函数：初始化卡牌属性
     */
    public Shrooms() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWER; // 设置基础效果值
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * @param p 玩家
     * @param m 目标敌人（此卡不与敌人直接互动，参数未使用）
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 循环执行抽卡和随机化费用的动作，次数取决于 magicNumber
        for (int i = 0; i < magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new ShroomsAction());
        }

        // 添加视觉效果：粉色的垂直光环特效，持续时间与 magicNumber 成正比
        AbstractDungeon.actionManager.addToBottom(
                new VFXAction(
                        AbstractDungeon.player,
                        new VerticalAuraEffect(Color.PINK, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY),
                        (float) 0.1 * magicNumber // 每张卡牌增加 0.1 秒的特效持续时间
                )
        );
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 Shrooms 实例
     */
    public AbstractCard makeCopy() {
        return new Shrooms();
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 增强卡牌效果：提升抽牌数量
     * - 升级卡牌名称以展示视觉差异
     */
    public void upgrade() {
        if (!upgraded) {                   // 检查是否已升级，防止重复升级
            upgradeName();                 // 升级卡牌名称，通常在名称后加“+”符号
            upgradeMagicNumber(UPGRADED_BONUS); // 提升 magicNumber，增加抽牌数量
        }
    }
}
