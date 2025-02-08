package witchmod.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.effects.PunctureEffect;

public class Puncture extends AbstractWitchCard {
    public static final String ID = "Puncture";  // 卡片ID
    public static final String NAME = "Puncture";  // 卡片名称
    public static final String IMG = "cards/puncture.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Deal !D! damage !M! times. NL Exhaust.";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;

    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 1;  // 基础伤害
    private static final int MAGIC = 5;  // 伤害次数
    private static final int UPGRADE_MAGIC_BONUS = 2;  // 升级后的伤害次数增加量

    public Puncture() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
        this.baseMagicNumber = this.magicNumber = MAGIC;  // 设置魔法数（伤害次数）
        this.exhaust = true;  // 使用后会被丢弃（exhaust）
    }

    // 使用卡片时的逻辑
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            // 每次攻击都会播放一次特效
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new PunctureEffect(m.hb.cX + MathUtils.random(-50.0f, 50.0f) * Settings.scale, m.hb.cY + MathUtils.random(-60.0f, 60.0f) * Settings.scale), 0.0075f));
            // 造成伤害
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.NONE));
        }
    }

    // 创建卡片副本
    public AbstractCard makeCopy() {
        return new Puncture();  // 返回副本
    }

    // 升级卡片时的行为
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(UPGRADE_MAGIC_BONUS);  // 升级魔法数，增加伤害次数
        }
    }
}
