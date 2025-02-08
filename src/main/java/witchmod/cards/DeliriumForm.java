package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.DeliriumFormPower;

public class DeliriumForm extends AbstractWitchCard {
    public static final String ID = "DeliriumForm";  // 卡片ID
    public static final String NAME = "Delirium Form";  // 卡片名称
    public static final String IMG = "cards/deliriumform.png";  // 卡片图片路径
    public static final String DESCRIPTION = "每当你使用一个非零费用的卡片时，抽一张卡。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自身
    private static final CardType TYPE = CardType.POWER;  // 卡片类型：力量

    private static final int POOL = 1;  // 卡池数量

    private static final int COST = 3;  // 初始能量消耗
    private static final int COST_UPGRADED = 2;  // 升级后的能量消耗
    private static final int POWER = 1;  // 魔法数（每当打出非零费用卡时抽卡的次数）

    public DeliriumForm() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWER;  // 设置基础魔法数
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用卡片时，应用 DeliriumFormPower（使每次打出非零费用卡时抽一张卡）
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DeliriumFormPower(magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DeliriumForm();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBaseCost(COST_UPGRADED);  // 升级能量消耗
        }
    }
}
