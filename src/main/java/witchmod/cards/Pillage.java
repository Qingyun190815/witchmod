package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.actions.ReduceBlockAction;

public class Pillage extends AbstractWitchCard {
    public static final String ID = "Pillage";  // 卡片ID
    public static final String NAME = "Pillage";  // 卡片名称
    public static final String IMG = "cards/pillage.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Remove up to !M! Block from an enemy. Gain Block equal to the amount removed plus !B!.";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;  // 目标：自己和敌人
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;

    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 8;  // 最大可偷取的敌人防御值
    private static final int UPGRADE_BONUS = 5;  // 升级后偷取的最大防御值增加
    private static final int BLOCK = 5;  // 自身防御增益
    private static final int UPGRADE_BLOCK_BONUS = 3;  // 升级后增加的防御值

    public Pillage() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = POWER;  // 设置魔法数为最大偷取的防御值
        this.baseBlock = BLOCK;  // 设置基础防御为BLOCK
    }

    // 使用卡片时的逻辑
    public void use(AbstractPlayer p, AbstractMonster m) {
        int stealableBlock = Math.min(m.currentBlock, magicNumber);  // 计算能偷取的最大防御值
        if (stealableBlock > 0) {
            // 如果能偷取到防御，则减少敌人的防御
            AbstractDungeon.actionManager.addToBottom(new ReduceBlockAction(m, p, stealableBlock));
        }
        // 为玩家自己增加偷取的防御值加上基础防御
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, stealableBlock + block));
    }

    // 创建卡片副本
    public AbstractCard makeCopy() {
        return new Pillage();  // 返回副本
    }

    // 升级卡片时的行为
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级魔法数（增加最大偷取防御）
            upgradeBlock(UPGRADE_BLOCK_BONUS);  // 升级防御值（增加基础防御）
        }
    }
}
