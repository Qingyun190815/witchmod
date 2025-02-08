package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.actions.CrystalResonanceAction;

public class CrystalResonance extends AbstractWitchCard {
    public static final String ID = "CrystalResonance";  // 卡片ID
    public static final String NAME = "Crystal Resonance";  // 卡片名称
    public static final String IMG = "cards/crystalresonance.png";  // 卡片图片路径
    public static final String DESCRIPTION = "当此卡被抽到时，减少手牌中所有技能的费用 !M!。 NL 获得 !B! 护盾。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int COST = 1;  // 卡片能量消耗
    private static final int POWER = 5;  // 初始护盾
    private static final int UPGRADE_BONUS = 3;  // 升级后增加的护盾
    private static final int MAGIC = 1;  // 魔法数值，减少技能费用的量

    public CrystalResonance() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置基础护盾
        this.baseMagicNumber = this.magicNumber = MAGIC;  // 设置减少技能费用的基础魔法数值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 为自己获得护盾
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new CrystalResonance();  // 返回卡片副本
    }

    @Override
    public void triggerWhenDrawn() {
        // 当此卡被抽到时，触发以下效果
        super.triggerWhenDrawn();
        flash();  // 闪光提示
        AbstractDungeon.actionManager.addToBottom(new CrystalResonanceAction());  // 执行CrystalResonanceAction动作
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(UPGRADE_BONUS);  // 升级护盾值
        }
    }
}
