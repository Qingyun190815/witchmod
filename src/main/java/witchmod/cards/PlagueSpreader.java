package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.RotPower;

public class PlagueSpreader extends AbstractWitchCard {
    public static final String ID = "PlagueSpreader";  // 卡片ID
    public static final String NAME = "Plague Spreader";  // 卡片名称
    public static final String IMG = "cards/plaguespreader.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Apply !M! Rot to ALL enemies. NL Exhaust.";  // 卡片描述
    public static final String DESCRIPTION_UPGRADED = "Innate. NL Apply !M! Rot to ALL enemies. NL Exhaust.";  // 升级后的描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  // 目标：所有敌人
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;

    private static final int COST = 0;  // 能量消耗
    private static final int MAGIC = 1;  // 基础腐烂层数

    public PlagueSpreader() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = MAGIC;  // 设置魔法数为腐烂层数
        this.exhaust = true;  // 标记该卡片在使用后会被弃掉
    }

    // 使用卡片时的逻辑
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人施加腐烂（Rot）效果
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (mo.isDead || mo.isDying) continue;  // 跳过死亡或濒死的敌人
            // 施加腐烂状态
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new RotPower(mo, p, magicNumber, false), magicNumber, true));
        }
    }

    // 创建卡片副本
    public AbstractCard makeCopy() {
        return new PlagueSpreader();  // 返回副本
    }

    // 升级卡片时的行为
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDescription();  // 升级卡片描述
            isInnate = true;  // 升级后使卡片成为天生卡片（开局就有）
        }
    }
}
