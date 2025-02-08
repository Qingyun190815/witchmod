package witchmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.effects.FastShockWaveEffect;

public class DireShriek extends AbstractWitchUnveilCard {
    public static final String ID = "DireShriek";  // 卡片ID
    public static final String NAME = "Dire Shriek";  // 卡片名称
    public static final String IMG = "cards/direshriek.png";  // 卡片图片路径
    public static final String DESCRIPTION = "当抽到时，造成 !M! 点伤害给所有敌人。 NL 对所有敌人造成 !D! 点伤害。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  // 目标：所有敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡池数量

    private static final int COST = 2;  // 初始能量消耗
    private static final int POWER = 9;  // 基础伤害
    private static final int UPGRADE_BONUS = 2;  // 升级后增加的伤害

    private static final int MAGIC = 6;  // 魔法数（抽到卡片时造成的伤害）

    public DireShriek() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
        this.baseMagicNumber = MAGIC;  // 设置基础魔法数
        this.isMultiDamage = true;  // 设置为多目标伤害
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放震荡波效果
        AbstractDungeon.actionManager.addToBottom(new VFXAction(p, new FastShockWaveEffect(p.hb.cX, p.hb.cY, Color.YELLOW, FastShockWaveEffect.ShockWaveType.NORMAL), 0.35f));
        // 对所有敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new DireShriek();  // 返回卡片副本
    }

    @Override
    public void unveil() {
        flash();  // 闪光
        CardCrawlGame.sound.play("BYRD_DEATH");  // 播放音效
        // 对所有敌人造成魔法数的伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(baseMagicNumber, true), DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级伤害
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级魔法数
        }
    }
}
