package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import witchmod.powers.DecrepitPower;

public class Decrepify extends AbstractWitchCard {
    public static final String ID = "Decrepify";  // 卡片ID
    public static final String NAME = "Decrepify";  // 卡片名称
    public static final String IMG = "cards/decrepify.png";  // 卡片图片路径
    public static final String DESCRIPTION = "随机对一个敌人施加 !M! 衰弱 X 次。";  // 卡片描述
    public static final String DESCRIPTION_UPGRADED = "随机对一个敌人施加 !M! 衰弱 X+1 次。";  // 升级后的卡片描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  // 目标：所有敌人
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 卡池数量
    private static final int COST = -1;  // 能量消耗为 -1（由当前能量决定）

    private static final int POWER = 2;  // 衰弱效果的强度

    public Decrepify() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER;  // 设置衰弱效果的基础强度
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获取当前能量
        int energy = EnergyPanel.getCurrentEnergy();
        // 根据是否升级决定衰弱应用次数
        int counter = upgraded ? energy + 1 : energy;
        // 循环施加衰弱
        while (counter > 0) {
            AbstractMonster monster = AbstractDungeon.getRandomMonster();  // 随机选择一个敌人
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monster, p, new DecrepitPower(monster, magicNumber, false), magicNumber, true));  // 施加衰弱
            counter--;
        }
        // 使用消耗的能量
        p.energy.use(energy);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Decrepify();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDescription();  // 升级卡片描述
        }
    }
}
