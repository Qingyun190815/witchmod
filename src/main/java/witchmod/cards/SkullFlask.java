package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect; // 定义攻击效果（如光效、击打特效等）
import com.megacrit.cardcrawl.actions.animations.VFXAction;            // 添加视觉特效的动作
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;         // 应用力量（Power）的动作
import com.megacrit.cardcrawl.actions.common.DamageAction;             // 造成伤害的动作
import com.megacrit.cardcrawl.actions.utility.SFXAction;               // 播放音效的动作
import com.megacrit.cardcrawl.cards.AbstractCard;                      // 卡牌的基础抽象类
import com.megacrit.cardcrawl.cards.DamageInfo;                        // 描述伤害的相关信息
import com.megacrit.cardcrawl.characters.AbstractPlayer;              // 玩家抽象类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;               // 游戏主逻辑控制类
import com.megacrit.cardcrawl.monsters.AbstractMonster;               // 敌人抽象类
import com.megacrit.cardcrawl.powers.StrengthPower;                   // 力量增益（Strength）的实现类
import witchmod.effects.SkullFlaskEffect;                             // 自定义的骷髅瓶视觉特效
import witchmod.powers.SkullFlaskPower;                               // 自定义的骷髅瓶效果

/**
 * Skull Flask（骷髅瓶）是一张攻击卡（Attack Card）：
 * - 效果：抽到这张卡时获得临时力量加成，使用时对敌人造成伤害。
 * - 附带自定义的视觉和音效效果，增强战斗氛围。
 */
public class SkullFlask extends AbstractWitchUnveilCard {
    // 卡牌的基础信息
    public static final String ID = "SkullFlask";                     // 唯一ID
    public static final String NAME = "Skull Flask";                 // 卡牌名称
    public static final String IMG = "cards/skullflask.png";         // 卡牌图片路径
    public static final String DESCRIPTION = "When drawn gain !M! Strength for 1 turn. NL Deal !D! damage.";
    // 描述：抽到时获得 !M! 点力量（持续 1 回合），使用时造成 !D! 点伤害。

    private static final CardRarity RARITY = CardRarity.COMMON;      // 稀有度：普通（Common）
    private static final CardTarget TARGET = CardTarget.ENEMY;       // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;            // 卡牌类型：攻击卡（Attack）

    private static final int POOL = 1;        // 冗余参数，未被实际使用，保留以便拓展
    private static final int COST = 1;        // 能量消耗：1 点
    private static final int POWER = 6;       // 基础伤害值：6 点

    private static final int MAGIC = 2;       // 基础力量加成：2 点
    private static final int MAGIC_UPGRADE_BONUS = 2; // 升级后力量加成增加 2 点（共 4 点）

    /**
     * 构造函数：初始化卡牌属性
     */
    public SkullFlask() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;                      // 设置基础伤害
        this.baseMagicNumber = this.magicNumber = MAGIC; // 设置基础力量加成
    }

    /**
     * use 方法：使用卡牌时触发的效果
     * @param p 玩家
     * @param m 目标敌人
     */
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 1. 添加骷髅瓶特效（瓶子从玩家飞向敌人）
        AbstractDungeon.actionManager.addToBottom(
                new VFXAction(new SkullFlaskEffect(p.hb.cY, p.hb.cX, m.hb.cX, m.hb.cY), 0.6f)
        );

        // 2. 播放击中时的血液飞溅音效
        AbstractDungeon.actionManager.addToBottom(new SFXAction("BLOOD_SPLAT"));

        // 3. 对敌人造成伤害（光击效果 BLUNT_LIGHT）
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.BLUNT_LIGHT)
        );
    }

    /**
     * makeCopy 方法：创建卡牌的副本
     * @return 新的 SkullFlask 实例
     */
    public AbstractCard makeCopy() {
        return new SkullFlask();
    }

    /**
     * unveil 方法：当这张卡牌被抽到时触发的特殊效果
     * - 获得 !M! 点力量，持续 1 回合
     * - 应用自定义的 SkullFlask 力量效果（具体效果取决于 SkullFlaskPower 的实现）
     */
    @Override
    public void unveil() {
        // 1. 立即获得力量加成（StrengthPower）
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, magicNumber), magicNumber)
        );

        // 2. 应用自定义的骷髅瓶力量（SkullFlaskPower）
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SkullFlaskPower(AbstractDungeon.player, magicNumber), magicNumber)
        );
    }

    /**
     * upgrade 方法：升级卡牌时触发
     * - 增强卡牌的力量加成效果
     * - 升级卡牌名称以展示视觉差异
     */
    public void upgrade() {
        if (!upgraded) {                   // 确保卡牌只被升级一次
            upgradeName();                 // 升级卡牌名称，通常在名称后加“+”符号
            upgradeMagicNumber(MAGIC_UPGRADE_BONUS); // 增加力量加成数值
        }
    }
}
