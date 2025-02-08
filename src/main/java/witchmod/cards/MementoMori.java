package witchmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.effects.ColoredSliceEffect;

public class MementoMori extends AbstractWitchCard {
    public static final String ID = "MementoMori";  // 卡片ID
    public static final String NAME = "Memento Mori";  // 卡片名称
    public static final String IMG = "cards/mementomori.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 !D! 点伤害，伤害值随目标缺失生命的百分比增加。 NL 折弃此卡。";  // 卡片描述
    private static final String DESCRIPTION_UPGRADE = "造成 !D! 点伤害，伤害值随目标缺失生命的百分比增加。 NL 折弃此卡，升级后增加伤害。";  // 升级后的描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 稀有度：普通
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡片池

    private static final int COST = 1;  // 卡片能量消耗
    private static final int POWER = 11;  // 卡片的基础伤害
    private static final int UPGRADE_BONUS = 5;  // 升级后的伤害加成

    public MementoMori() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
        this.exhaust = true;  // 使用后卡片会被消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放特效
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ColoredSliceEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY, Color.RED.cpy()), 0.25f));
        // 对目标造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.POISON));
    }

    @Override
    public AbstractCard makeCopy() {
        return new MementoMori();  // 返回卡片副本
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
        if (mo == null || mo.maxHealth == 0) {
            return tmp;  // 如果目标不存在或最大生命值为零，返回原始伤害
        }
        // 计算目标当前生命值与最大生命值的百分比
        float percent = (float) mo.currentHealth / mo.maxHealth;
        // 根据目标缺失的生命百分比增加伤害
        return tmp * (2f - percent);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级时增加伤害
            rawDescription = DESCRIPTION_UPGRADE;  // 更新卡片描述
            initializeDescription();  // 初始化描述
        }
    }
}
