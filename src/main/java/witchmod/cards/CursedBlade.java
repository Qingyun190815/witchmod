package witchmod.cards;

// 导入必要的类库
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

// 定义卡牌类 CursedBlade，继承自 AbstractWitchCard（自定义的卡牌基类）
public class CursedBlade extends AbstractWitchCard {
    // 卡牌的唯一标识符
    public static final String ID = "CursedBlade";
    // 卡牌名称
    public static final String NAME = "Cursed Blade";
    // 卡牌图片路径
    public static final String IMG = "cards/cursedblade.png";
    // 卡牌描述，!D! 会被替换为实际伤害值
    public static final String DESCRIPTION = "Deal !D! damage. Add a random Curse to your discard pile.";

    // 卡牌的稀有度（UNCOMMON）
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    // 卡牌的目标（敌人）
    private static final CardTarget TARGET = CardTarget.ENEMY;
    // 卡牌类型（攻击）
    private static final CardType TYPE = CardType.ATTACK;

    // 卡牌池编号（通常用于区分不同卡池）
    private static final int POOL = 1;

    // 卡牌使用费用
    private static final int COST = 1;
    // 卡牌基础伤害值
    private static final int POWER = 13;
    // 卡牌升级后的伤害加成
    private static final int UPGRADE_BONUS = 4;

    // 构造函数
    public CursedBlade() {
        // 调用父类构造函数，传入卡牌ID、图片路径、费用、类型、稀有度和目标
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        // 设置卡牌的基础伤害值
        this.baseDamage = POWER;
    }

    // 卡牌使用时的逻辑
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 添加一个伤害动作，对目标敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m,
                        new DamageInfo(p, damage, damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL) // 攻击特效
        );
        // 添加一个动作，将一张随机诅咒牌放入弃牌堆
        AbstractDungeon.actionManager.addToBottom(
                new MakeTempCardInDiscardAction(AbstractDungeon.returnRandomCurse(), 1)
        );
    }

    // 创建卡牌的副本
    public AbstractCard makeCopy() {
        return new CursedBlade();
    }

    // 卡牌升级逻辑
    public void upgrade() {
        if (!upgraded) {
            // 调用父类方法，标记卡牌已升级
            upgradeName();
            // 提升卡牌伤害值
            upgradeDamage(UPGRADE_BONUS);
        }
    }
}