package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.GrimVengeancePower;

public class GrimVengeance extends AbstractWitchCard {
    public static final String ID = "GrimVengeance";  // 卡片ID
    public static final String NAME = "Grim Vengeance";  // 卡片名称
    public static final String IMG = "cards/grimvengeance.png";  // 卡片图片路径
    public static final String DESCRIPTION = "当你被攻击时，向攻击者施加 !M! 衰弱效果。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.POWER;  // 卡片类型：力量

    private static final int POOL = 1;  // 卡池数量
    private static final int COST = 1;  // 能量消耗

    private static final int POWER = 1;  // 初始力量数值
    private static final int UPGRADE_BONUS = 1;  // 升级后增加的力量数值

    public GrimVengeance() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER;  // 初始化力量数值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用时，为自己施加 "GrimVengeancePower" 力量，效果是被攻击时对攻击者施加衰弱效果
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GrimVengeancePower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GrimVengeance();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级后增加力量数值
        }
    }
}
