package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.BalefulWardPower;

public class BalefulWard extends AbstractWitchCard {
    public static final String ID = "BalefulWard";  // 卡片ID
    public static final String NAME = "不祥护符";  // 卡片名称
    public static final String IMG = "cards/balefulward.png";  // 卡片图片路径
    public static final String DESCRIPTION = "获得 !B! 点格挡. NL 如果本回合格挡被打破,向你的手牌中添加一张此卡的复制.";  // 描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 池（数量）

    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 8;  // 初始护盾
    private static final int UPGRADE_BONUS = 3;  // 升级后增加的护盾数值

    public BalefulWard() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置卡片基础护盾数值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 为玩家提供护盾
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        // 应用一个效果（`BalefulWardPower`）到玩家身上，用来追踪该卡片的效果
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new BalefulWardPower(this), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new BalefulWard();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(UPGRADE_BONUS);  // 升级护盾数值
        }
    }
}
