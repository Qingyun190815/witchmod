package witchmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.IronWaveEffect;
import witchmod.WitchMod;

public class EvilEye extends AbstractWitchCard {
    public static final String ID = "EvilEye";  // 卡片ID
    public static final String NAME = "Evil Eye";  // 卡片名称
    public static final String IMG = "cards/evileye.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 !D! 点伤害。 NL 每抽到一张诅咒牌，额外造成 !M! 点伤害。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡池数量

    private static final int COST = 1;  // 初始能量消耗
    private static final int POWER = 6;  // 基础伤害
    private static final int MAGIC = 2;  // 每张诅咒牌额外的伤害
    private static final int MAGIC_UPGRADE_BONUS = 1;  // 升级后增加的额外伤害

    public EvilEye() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
        this.magicNumber = this.baseMagicNumber = MAGIC;  // 设置每张诅咒牌造成的额外伤害
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放红色边框闪光效果
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.RED)));
        // 播放铁波特效
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new IronWaveEffect(p.hb.cX, p.hb.cY, m.hb.cX), 0.25f));
        // 对敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
        // 根据当前抽到的诅咒牌数量来增加额外伤害
        return tmp + magicNumber * WitchMod.cursesDrawnTotal;
    }

    @Override
    public AbstractCard makeCopy() {
        return new EvilEye();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(MAGIC_UPGRADE_BONUS);  // 升级每张诅咒牌的额外伤害
        }
    }
}
