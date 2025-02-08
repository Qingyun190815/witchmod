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
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;

public class FatalRay extends AbstractWitchCard {
    public static final String ID = "FatalRay";  // 卡片ID
    public static final String NAME = "Fatal Ray";  // 卡片名称
    public static final String IMG = "cards/fatalray.png";  // 卡片图片路径
    public static final String DESCRIPTION = "至少有7张牌在手时才能使用。造成 !D! 点伤害。";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡池数量

    private static final int COST = 2;  // 初始能量消耗
    private static final int POWER = 25;  // 基础伤害
    private static final int UPGRADE_BONUS = 10;  // 升级后增加的伤害

    public FatalRay() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放红色边框闪光效果
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SCARLET), 0.3f));
        // 播放心灵爆发特效
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new MindblastEffect(p.hb.cX, p.hb.cY, false), 0.3f));
        // 对敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        // 判断是否有至少7张牌在手，才能使用这张卡
        if (p.hand.group.size() >= 7) {
            return super.canUse(p, m);
        } else {
            cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];  // 显示无法使用的消息
            return false;
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FatalRay();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级伤害
        }
    }
}
