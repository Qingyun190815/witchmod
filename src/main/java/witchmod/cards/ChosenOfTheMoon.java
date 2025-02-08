package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.ChosenOfTheMoonPower;

public class ChosenOfTheMoon extends AbstractWitchCard {
    public static final String ID = "ChosenOfTheMoon";  // 卡片ID
    public static final String NAME = "Moon Chosen";  // 卡片名称
    public static final String IMG = "cards/chosenofthemoon.png";  // 卡片图片路径
    public static final String DESCRIPTION = "回合开始时，如果你没有 月亮祝福 ，获得 !M! 月亮祝福。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.POWER;  // 卡片类型：能力

    private static final int COST = 1;  // 卡片能量消耗
    private static final int POWER = 1;  // 初始 Artifact 数量
    private static final int UPGRADE_BONUS = 1;  // 升级后增加的 Artifact 数量

    public ChosenOfTheMoon() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER;  // 设置基础魔法数值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放音效
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_AWAKENEDONE_1", 0.3f));
        // 给玩家施加 ChosenOfTheMoonPower
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ChosenOfTheMoonPower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new ChosenOfTheMoon();  // 返回一张新副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级魔法数值
        }
    }
}
