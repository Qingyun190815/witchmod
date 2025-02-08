package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Harmlessness extends AbstractWitchCleansableCurse {
    public static final String ID = "Harmlessness";  // 卡片ID
    public static final String NAME = "Harmlessness";  // 卡片名称
    public static final String NAME_CLEANSED = "\"Harmlessness\"";  // 清除后的名称
    public static final String IMG = "cards/harmlessness.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Unplayable. NL Cleanse: play 3 skills this turn.";  // 未清除的描述
    public static final String DESCRIPTION_CLEANSED = "Deal !D! damage to a random enemy !M! times.";  // 清除后的描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  // 目标：所有敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int COST = 1;  // 能量消耗
    private static final int THRESHOLD = 3;  // 清除条件：至少使用3张技能卡

    private static final int DAMAGE = 5;  // 默认伤害
    private static final int MAGIC = 5;  // 魔法数：影响攻击次数

    public Harmlessness(boolean hasCardPreview) {
        super(ID, IMG, RARITY);
        this.baseDamage = DAMAGE;  // 设置基础伤害
        this.baseMagicNumber = MAGIC;  // 设置魔法数
        this.checkAtTurnStart = false;  // 禁用回合开始时检查
        if (hasCardPreview) {
            Harmlessness tmp = new Harmlessness(false);
            tmp.cleanse(false);
            cardPreviewTooltip = tmp;  // 设置卡片预览
        }
    }

    public Harmlessness() {
        this(true);  // 调用有卡片预览的构造函数
    }

    @Override
    public void cleanse(boolean applyPowers) {
        // 清除后的设置
        type = TYPE;  // 设置卡片类型为攻击
        cost = COST;  // 设置能量消耗
        costForTurn = COST;  // 设置回合中的能量消耗
        isCostModified = false;  // 能量消耗不受影响
        target = TARGET;  // 设置卡片目标为所有敌人
        name = cardStrings.EXTENDED_DESCRIPTION[0];  // 设置卡片名称为清除后的名称
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;  // 设置清除后的描述
        initializeDescription();  // 初始化描述
        super.cleanse(applyPowers);  // 调用父类的清除方法
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查玩家是否有蓝色蜡烛遗物并且卡片未清除
        if (!dontTriggerOnUseCard && p.hasRelic("Blue Candle") && !cleansed) {
            useBlueCandle(p);  // 使用蓝色蜡烛的效果
        } else {
            // 清除后，攻击随机敌人多次
            for (int i = 0; i < baseMagicNumber; i++) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.getRandomMonster(), new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_DIAGONAL, true));
            }
        }
    }

    @Override
    protected boolean cleanseCheck() {
        // 检查本回合是否使用了至少3张技能卡
        int count = 0;
        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type == CardType.SKILL) {
                count++;
            }
        }
        return count >= THRESHOLD;  // 如果使用了3张技能卡，返回true
    }

    @Override
    public AbstractCard makeCopy() {
        return new Harmlessness();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        // 升级后没有变化
    }
}
