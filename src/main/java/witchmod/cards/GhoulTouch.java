package witchmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.actions.GhoulTouchAction;

public class GhoulTouch extends AbstractWitchCard {
    public static final String ID = "GhoulTouch";  // 卡片ID
    public static final String NAME = "Ghoul Touch";  // 卡片名称
    public static final String IMG = "cards/ghoultouch.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 !D! 点伤害，然后敌人失去等同于未抵挡伤害的力量 1 回合。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡池数量
    private static final int COST = 2;  // 初始能量消耗
    private static final int POWER = 7;  // 初始伤害
    private static final int UPGRADE_BONUS = 3;  // 升级后伤害加成

    public GhoulTouch() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 执行 GhoulTouchAction，造成伤害并使敌人失去力量
        AbstractDungeon.actionManager.addToBottom(new GhoulTouchAction(m, new DamageInfo(p, damage)));
    }

    @Override
    public AbstractCard makeCopy() {
        return new GhoulTouch();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级后增加伤害
        }
    }
}
