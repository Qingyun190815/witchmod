package witchmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.actions.RiteOfAutumnAction;

public class RiteOfAutumn extends AbstractWitchCard {
    public static final String ID = "RiteOfAutumn";  // 卡片ID
    public static final String NAME = "Rite of Autumn";  // 卡片名称
    public static final String IMG = "cards/riteofautumn.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Exhaust any number of cards in your hand and draw that many cards. NL Exhaust.";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 池中的卡片数量
    private static final int COST = 1;  // 能量消耗
    private static final int COST_UPGRADED = 0;  // 升级后能量消耗

    public RiteOfAutumn() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.exhaust = true;  // 使用后卡片会被丢弃
    }

    // 使用卡片时的逻辑
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new RiteOfAutumnAction());  // 执行 RiteOfAutumnAction（自定义行为）
    }

    // 创建卡片副本
    public AbstractCard makeCopy() {
        return new RiteOfAutumn();  // 返回副本
    }

    // 升级时的行为
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBaseCost(COST_UPGRADED);  // 升级能量消耗
        }
    }
}
