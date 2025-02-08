package witchmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.actions.RiteOfWinterAction;

public class RiteOfWinter extends AbstractWitchCard {
    public static final String ID = "RiteOfWinter";  // 卡片ID
    public static final String NAME = "Rite Of Winter";  // 卡片名称
    public static final String IMG = "cards/riteofwinter.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Place any number of cards in your hand on top of your draw pile and gain !B! Block for each of them.";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 池中的卡片数量
    private static final int COST = 2;  // 能量消耗
    private static final int BLOCK = 5;  // 每张卡片带来的基础护盾
    private static final int BLOCK_UPGRADED = 3;  // 升级后额外的护盾量

    public RiteOfWinter() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = BLOCK;  // 设置基础护盾
    }

    // 使用卡片时的逻辑
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new RiteOfWinterAction(block));  // 执行 RiteOfWinterAction（自定义行为），传递基础护盾值
    }

    // 创建卡片副本
    public AbstractCard makeCopy() {
        return new RiteOfWinter();  // 返回副本
    }

    // 升级时的行为
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(BLOCK_UPGRADED);  // 升级护盾量
        }
    }
}
