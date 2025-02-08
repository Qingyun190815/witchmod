package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.IllOmenPower;

public class IllOmen extends AbstractWitchCard {
    public static final String ID = "IllOmen";
    public static final String NAME = "Ill Omen";  // 卡片名称
    public static final String IMG = "cards/illomen.png";  // 卡片图片路径
    public static final String DESCRIPTION = "将一张随机的诅咒卡片洗入抽牌堆。下次抽到诅咒卡时，对所有敌人造成 !M! 点伤害。 该卡会被耗尽。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.NONE;  // 目标：无目标
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 卡片池（对初始卡池无影响）

    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 14;  // 默认伤害
    private static final int UPGRADE_BONUS = 4;  // 升级后的伤害增加

    public IllOmen() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER;  // 设置基础魔法数（伤害）
        this.exhaust = true;  // 卡片使用后被耗尽
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用卡片时，应用 IllOmenPower 给自己，并将随机诅咒卡放入抽牌堆
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new IllOmenPower(p, magicNumber), magicNumber));  // 添加 IllOmenPower
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(AbstractDungeon.returnRandomCurse(), 1, true, true));  // 将随机诅咒卡片洗入抽牌堆
    }

    public AbstractCard makeCopy() {
        return new IllOmen();  // 返回卡片副本
    }

    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级后增加伤害
        }
    }
}
