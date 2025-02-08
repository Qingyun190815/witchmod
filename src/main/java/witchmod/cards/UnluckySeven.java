package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class UnluckySeven extends AbstractWitchCleansableCurse {
    public static final String ID = "UnluckySeven";  // 卡片ID
    public static final String NAME = "Unlucky Seven";  // 卡片名称
    public static final String NAME_CLEANSED = "Lucky Seven";  // 清除后的卡片名称
    public static final String IMG = "cards/luckyseven.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Unplayable. NL Cleanse: have your current health ending with 7.";  // 未清除时的描述
    public static final String DESCRIPTION_CLEANSED = "Gain !M! [B]. NL Draw !M! cards. NL Lose !M! health. NL Exhaust.";  // 清除后的描述
    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int COST = 0;  // 能量消耗
    private static final int POWER = 7;  // 能量、抽卡数与伤害数值

    public UnluckySeven(boolean hasCardPreview) {
        super(ID, IMG, RARITY);
        this.baseMagicNumber = this.magicNumber = POWER;  // 设置魔法数（能量、抽卡数、伤害数值）
        this.exhaust = true;  // 使用后卡片会被消耗
        if (hasCardPreview) {
            UnluckySeven tmp = new UnluckySeven(false);
            tmp.cleanse(false);  // 清除卡片预览
            cardPreviewTooltip = tmp;
        }
    }

    public UnluckySeven() {
        this(true);  // 调用有卡片预览的构造函数
    }

    @Override
    public void cleanse(boolean applyPowers) {
        // 清除后的设置
        type = TYPE;  // 设置卡片类型为技能
        cost = COST;  // 设置能量消耗
        costForTurn = COST;  // 设置回合中的能量消耗
        isCostModified = false;  // 能量消耗不受影响
        target = TARGET;  // 目标为自己
        name = cardStrings.EXTENDED_DESCRIPTION[0];  // 清除后的名称
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;  // 设置清除后的描述
        initializeDescription();  // 初始化描述
        super.cleanse(applyPowers);  // 调用父类的清除方法
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查玩家是否有蓝色蜡烛遗物并且卡片未清除
        if (!dontTriggerOnUseCard && p.hasRelic("Blue Candle") && !cleansed) {
            this.useBlueCandle(p);  // 使用蓝色蜡烛的效果
        } else {
            // 清除后，给予玩家能量、抽卡并造成伤害
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(magicNumber));  // 获得能量
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));  // 抽取卡片
            AbstractDungeon.actionManager.addToBottom(new DamageAction(p, new DamageInfo(p, magicNumber, DamageType.HP_LOSS)));  // 造成伤害
        }
    }

    @Override
    protected boolean cleanseCheck() {
        // 检查玩家当前血量的个位数是否为7
        int lastDigit = AbstractDungeon.player.currentHealth % 10;
        return lastDigit == 7;  // 如果个位数是7，返回true
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnluckySeven();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        // 升级后没有变化
    }
}
