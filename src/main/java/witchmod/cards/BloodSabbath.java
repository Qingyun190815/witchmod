package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ScreenOnFireEffect;
import witchmod.powers.DecrepitPower;

public class BloodSabbath extends AbstractWitchCard {
    public static final String ID = "BloodSabbath";  // 卡片ID
    public static final String NAME = "Blood Sabbath";  // 卡片名称
    public static final String IMG = "cards/bloodsabbath.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Deal !D! damage and apply !M! Decrepit to ALL enemies.";  // 描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  // 目标：所有敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 池（数量）

    private static final int COST = 3;  // 能量消耗
    private static final int POWER = 15;  // 初始伤害
    private static final int UPGRADE_BONUS = 5;  // 升级后增加的伤害
    private static final int MAGIC = 4;  // 初始Decrepit效果强度
    private static final int UPGRADE_MAGIC_BONUS = 3;  // 升级后Decrepit效果的强度增加

    public BloodSabbath() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
        this.baseMagicNumber = this.magicNumber = MAGIC;  // 设置Decrepit效果强度
        this.isMultiDamage = true;  // 启用多目标伤害
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放火焰特效
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new ScreenOnFireEffect(), 1.0f));

        // 对所有敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, multiDamage, DamageType.NORMAL, AbstractGameAction.AttackEffect.FIRE));

        // 为所有敌人施加Decrepit效果
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new DecrepitPower(mo, magicNumber, false), magicNumber, true, AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BloodSabbath();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级伤害
            upgradeMagicNumber(UPGRADE_MAGIC_BONUS);  // 升级Decrepit效果强度
        }
    }
}
