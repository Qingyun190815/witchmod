package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GnarledBody extends AbstractWitchCard {
    public static final String ID = "GnarledBody";  // 卡片ID
    public static final String NAME = "Gnarled Body";  // 卡片名称
    public static final String IMG = "cards/gnarledbody.png";  // 卡片图片路径
    public static final String DESCRIPTION = "每张手牌减少 1 点能量消耗。 NL 获得 !B! 点护甲。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 卡池数量
    private static final int COST = 7;  // 初始能量消耗
    private static final int POWER = 10;  // 初始护甲值
    private static final int UPGRADE_BONUS = 5;  // 升级后护甲加成

    public GnarledBody() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置基础护甲
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用时，获得护甲
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GnarledBody();  // 返回卡片副本
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        // 计算卡片消耗，基于手牌数量减少消耗
        setCostForTurn(Math.max(0, COST - AbstractDungeon.player.hand.group.size()));
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c) {
        // 每次有卡片打出时更新能量消耗
        setCostForTurn(Math.max(0, COST - AbstractDungeon.player.hand.group.size()));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(UPGRADE_BONUS);  // 升级后增加护甲
        }
    }
}
