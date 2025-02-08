package witchmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.actions.CorruptBloodAction;

public class CorruptBlood extends AbstractWitchCard {
    public static final String ID = "CorruptBlood";  // 卡片ID
    public static final String NAME = "Corrupt Blood";  // 卡片名称
    public static final String IMG = "cards/corruptblood.png";  // 卡片图片路径
    public static final String DESCRIPTION = "如果敌人中毒,将所有中毒转化为 `witchmod:rot`,否则施加 !M! 点中毒. witchmod:Recurrent.";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int COST = 1;  // 卡片能量消耗
    private static final int POWER = 3;  // 初始毒素数值
    private static final int UPGRADE_BONUS = 2;  // 升级后增加的毒素数值

    public CorruptBlood() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWER;  // 设置基础魔法数值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        reshuffleOnUse = true;  // 使用时会重新洗牌
        AbstractDungeon.actionManager.addToBottom(new CorruptBloodAction(m, p, magicNumber));  // 执行 CorruptBloodAction
    }

    @Override
    public AbstractCard makeCopy() {
        return new CorruptBlood();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级魔法数值
        }
    }
}
