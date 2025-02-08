package witchmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.effects.IgniteEffect;
import witchmod.powers.DemonfyrePower;

public class Demonfyre extends AbstractWitchCard {
    public static final String ID = "Demonfyre";  // 卡片ID
    public static final String NAME = "Demonfyre";  // 卡片名称
    public static final String IMG = "cards/demonfyre_by_mnmix.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 !D! 点伤害。本次战斗中所有Demonfyre卡片造成额外 !M! 点伤害。NL 可重复使用。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡池数量

    private static final int COST = 1;  // 初始能量消耗
    private static final int POWER = 5;  // 基础伤害
    private static final int MAGIC = 1;  // 魔法数（每次使用此卡会增加的伤害）
    private static final int UPGRADE_BONUS = 1;  // 升级后增加的魔法数

    public Demonfyre() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
        this.baseMagicNumber = this.magicNumber = MAGIC;  // 设置基础魔法数
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        reshuffleOnUse = true;
        // 进行伤害行动
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        // 添加点燃效果
        AbstractDungeon.effectsQueue.add(new IgniteEffect(m.hb.cX, m.hb.cY, Color.PURPLE, 35));
        CardCrawlGame.sound.play("ATTACK_FIRE");  // 播放火焰攻击音效
        // 应用Demonfyre力量（增加后续Demonfyre卡片的伤害）
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DemonfyrePower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Demonfyre();  // 返回卡片副本
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
        // 计算该卡片伤害的增幅，Demonfyre力量会增加伤害
        int bonus = AbstractDungeon.player.getPower(DemonfyrePower.POWER_ID_FULL) == null ? 0 : AbstractDungeon.player.getPower(DemonfyrePower.POWER_ID_FULL).amount;
        if (bonus > 0) {
            isDamageModified = true;  // 如果伤害被修改，标记为已修改
        }
        return tmp + bonus;  // 返回增加后的伤害
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeMagicNumber(UPGRADE_BONUS);  // 升级魔法数
        }
    }
}
