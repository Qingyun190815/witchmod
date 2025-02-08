package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RoilingBarrier extends AbstractWitchUnveilCard {
    public static final String ID = "RoilingBarrier";  // 卡片ID
    public static final String NAME = "Roiling Barrier";  // 卡片名称
    public static final String IMG = "cards/roilingbarrier.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Gain !B! Block. When drawn increase this card's Block by !M!. NL Recurrent.";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 池中的卡片数量

    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 5;  // 基础护盾值

    private static final int MAGIC = 1;  // 每次抽到卡片时增加的护盾
    private static final int MAGIC_UPGRADE_BONUS = 1;  // 升级后的额外护盾值

    public RoilingBarrier() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置基础护盾值
        this.baseMagicNumber = this.magicNumber = MAGIC;  // 设置每次抽到时增加的护盾
    }

    // 使用卡片时的逻辑
    public void use(AbstractPlayer p, AbstractMonster m) {
        reshuffleOnUse = true;  // 设置卡片使用时重新洗牌
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));  // 为玩家增加护盾
    }

    // 每当这张卡片被抽到时，增加卡片的护盾
    @Override
    public void unveil() {
        baseBlock += magicNumber;  // 增加护盾值
        isBlockModified = true;  // 标记护盾值已经被修改
    }

    // 创建卡片副本
    public AbstractCard makeCopy() {
        return new RoilingBarrier();  // 返回副本
    }

    // 升级时的行为
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(MAGIC_UPGRADE_BONUS);  // 增加每次抽到时的护盾提升量
        }
    }
}
