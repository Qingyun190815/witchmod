package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.HexguardPower;

public class Hexguard extends AbstractWitchCard {
    public static final String ID = "Hexguard";
    public static final String NAME = "Hexguard";  // 卡片名称
    public static final String IMG = "cards/hexguard.png";  // 卡片图片路径
    public static final String DESCRIPTION = "获得 !B! 额外护甲和 !M! 魔法护盾（Artifact） 1 回合。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.BASIC;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 0;  // 卡片池（对初始卡池无影响）

    private static final int COST = 2;  // 能量消耗
    private static final int POWER = 11;  // 默认护甲
    private static final int UPGRADE_BONUS = 4;  // 升级后的护甲增加
    private static final int MAGIC = 1;  // 魔法数：影响Artifact的数量

    public Hexguard() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置基础护甲
        this.baseMagicNumber = this.magicNumber = MAGIC;  // 设置基础魔法数（Artifact）
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给自己添加护甲和Artifact（魔法护盾）
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));  // 添加护甲
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new HexguardPower(p, magicNumber), magicNumber));  // 添加Artifact
    }

    public AbstractCard makeCopy() {
        return new Hexguard();  // 返回卡片副本
    }

    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(UPGRADE_BONUS);  // 升级后增加护甲
        }
    }
}
