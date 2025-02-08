package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Defend_Witch extends AbstractWitchCard {
    public static final String ID = "Defend_Witch";  // 卡片ID
    public static final String NAME = "Defend";  // 卡片名称
    public static final String IMG = "cards/defend.png";  // 卡片图片路径
    public static final String DESCRIPTION = "获得 !B! 点护甲。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.BASIC;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自身
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 0;  // 卡池数量

    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 5;  // 初始护甲值
    private static final int UPGRADE_BONUS = 3;  // 升级后护甲增加的值

    public Defend_Witch() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置基础护甲值
        this.tags.add(CardTags.STARTER_DEFEND);  // 添加卡片标签（初始防御卡）
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用卡片时，获得护甲
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Defend_Witch();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(UPGRADE_BONUS);  // 升级护甲增加的值
        }
    }
}
