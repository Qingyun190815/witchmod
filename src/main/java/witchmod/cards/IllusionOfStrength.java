package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class IllusionOfStrength extends AbstractWitchCard {
    public static final String ID = "IllusionOfStrength";
    public static final String NAME = "Illusory Strength";  // 卡片名称
    public static final String IMG = "cards/illusionofstrength.png";  // 卡片图片路径
    public static final String DESCRIPTION = "获得 !M! 点力量。将一张 \"Strength的错觉\" 卡片洗入你的抽牌堆。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.POWER;  // 卡片类型：力量

    private static final int POOL = 1;  // 卡片池（对初始卡池无影响）

    private static final int COST = 1;  // 能量消耗

    private static final int POWER = 4;  // 默认获得的力量
    private static final int UPGRADE_BONUS = 2;  // 升级后的力量增益

    public IllusionOfStrength() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER;  // 设置基础魔法数（力量）
        cardPreviewTooltip = new IllusionOfStrengthCurse();  // 设置卡片预览为错觉力量诅咒
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用卡片时，添加错觉力量诅咒卡到抽牌堆，并为玩家增加力量
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new IllusionOfStrengthCurse(), 1, true, false));  // 将错觉力量诅咒洗入抽牌堆
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));  // 为玩家增加力量
    }

    public AbstractCard makeCopy() {
        return new IllusionOfStrength();  // 返回卡片副本
    }

    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级后增加力量
        }
    }
}
