package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class EternalThirst extends AbstractWitchCleansableCurse {
    public static final String ID = "EternalThirst";  // 卡片ID
    public static final String NAME = "Eternal Thirst";  // 卡片名称
    public static final String IMG = "cards/eternalthirst.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Unplayable. NL Cleanse: suffer at least 20 damage in this fight.";  // 未清除的描述
    public static final String DESCRIPTION_CLEANSED = "Deal !D! damage, then heal for the unblocked damage dealt.";  // 清除后的描述
    public static final String[] EXTENDED_DESCRIPTION = new String[]{" NL You have suffered ", " damage."};  // 扩展描述
    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击
    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 10;  // 默认伤害

    public EternalThirst(boolean hasCardPreview) {
        super(ID, IMG, RARITY);
        this.baseDamage = POWER;  // 设置基础伤害
        if (hasCardPreview) {
            EternalThirst tmp = new EternalThirst(false);
            tmp.cleanse(false);  // 清除状态时不应用效果
            cardPreviewTooltip = tmp;  // 设置预览卡片
        }
        this.tags.add(CardTags.HEALING);  // 添加治疗标签
    }

    public EternalThirst() {
        this(true);  // 调用有卡片预览的构造函数
    }

    @Override
    public void cleanse(boolean applyPowers) {
        // 清除后的设置
        type = TYPE;  // 设置卡片类型为攻击
        cost = COST;  // 设置能量消耗
        costForTurn = COST;  // 设置回合中的能量消耗
        isCostModified = false;  // 能量消耗不受影响
        target = TARGET;  // 设置卡片目标为敌人
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;  // 使用升级后的描述
        initializeDescription();  // 初始化描述
        super.cleanse(applyPowers);  // 调用父类的清除方法
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查玩家是否有蓝色蜡烛遗物并且卡片没有清除
        if (!dontTriggerOnUseCard && p.hasRelic("Blue Candle") && !cleansed) {
            useBlueCandle(p);  // 使用蓝色蜡烛的效果
        } else {
            // 如果清除了，使用 VampireDamageAction 攻击敌人并治疗
            AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
        }
    }

    @Override
    protected boolean cleanseCheck() {
        // 检查战斗中是否已受到至少20点伤害
        return GameActionManager.damageReceivedThisCombat >= 20;
    }

    @Override
    public void tookDamage() {
        // 在玩家受到伤害后更新卡片描述
        rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0] + GameActionManager.damageReceivedThisCombat + cardStrings.EXTENDED_DESCRIPTION[1];
        initializeDescription();  // 更新描述
    }

    @Override
    public AbstractCard makeCopy() {
        return new EternalThirst();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        // 升级后没有变化
    }
}
