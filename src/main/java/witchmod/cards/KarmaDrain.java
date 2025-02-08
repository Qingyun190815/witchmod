package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class KarmaDrain extends AbstractWitchCard {
    public static final String ID = "KarmaDrain";  // 卡片ID
    public static final String NAME = "Karma Drain";  // 卡片名称
    public static final String IMG = "cards/karmadrain.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 X 层虚弱。为自己获得 !B! 阻挡 X 次。";  // 卡片描述
    private static final String DESCRIPTION_UPGRADE = "造成 X 层虚弱。为自己获得 !B! 阻挡 X 次，升级后阻挡加值增加。";  // 升级后描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 稀有度：普通
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;  // 目标：敌人和自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 卡片池

    private static final int COST = -1;  // 能量消耗为 -1，代表该卡片的能量消耗取决于当前能量
    private static final int POWER = 4;  // 默认阻挡值
    private static final int UPGRADE_BONUS = 2;  // 升级后的阻挡加成

    public KarmaDrain() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置基础阻挡值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.getCurrentEnergy();  // 获取当前能量
        if (effect > 0) {
            // 对敌人造成虚弱，虚弱层数等于当前能量
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, effect, false), effect));
            // 为自己获得阻挡效果，次数为当前能量
            for (int i = 0; i < effect; i++) {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
            }
        }
        p.energy.use(effect);  // 使用能量
    }

    @Override
    public AbstractCard makeCopy() {
        return new KarmaDrain();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(UPGRADE_BONUS);  // 升级时增加阻挡值
            rawDescription = DESCRIPTION_UPGRADE;  // 更新卡片描述
            initializeDescription();  // 初始化描述
        }
    }
}
