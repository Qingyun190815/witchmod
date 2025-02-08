package witchmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import witchmod.effects.DarkboltEffect;

public class BlackBolt extends AbstractWitchCard {
    public static final String ID = "BlackBolt";  // 卡片ID
    public static final String NAME = "黑色闪电";  // 卡片名称
    public static final String IMG = "cards/blackbolt.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 !D! 点伤害.如果手中有诅咒卡,给敌人施加 !M! 点弱化.";  // 描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 池（数量）

    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 9;  // 初始伤害
    private static final int UPGRADE_BONUS = 4;  // 升级后增加的伤害数值

    private static final int MAGIC = 2;  // 初始虚弱值
    private static final int MAGIC_UPGRADE_BONUS = 2;  // 升级后虚弱值增加

    public BlackBolt() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置卡片基础伤害数值
        this.baseMagicNumber = this.magicNumber = MAGIC;  // 设置虚弱值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 播放音效
        AbstractDungeon.actionManager.addToBottom(new SFXAction("GHOST_ORB_IGNITE_1", 0.3f));

        // 显示黑色闪电视觉效果
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new DarkboltEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY, Color.BLACK), 0.25f));

        // 对敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn)));

        // 检查玩家手牌中是否有诅咒卡
        boolean hasCurse = false;
        for (AbstractCard c : p.hand.group) {
            if (c.type == CardType.CURSE) {
                hasCurse = true;
                break;
            }
        }

        // 如果有诅咒卡，则对敌人施加虚弱效果
        if (hasCurse) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(p, magicNumber, false), magicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BlackBolt();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(UPGRADE_BONUS);  // 升级伤害
            upgradeMagicNumber(MAGIC_UPGRADE_BONUS);  // 升级虚弱值
        }
    }
}
