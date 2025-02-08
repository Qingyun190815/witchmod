package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MagicFang extends AbstractWitchCard {
    public static final String ID = "MagicFang";  // 卡片ID
    public static final String NAME = "Runic Fang";  // 卡片名称
    public static final String IMG = "cards/magicfang.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 !D! 点伤害两次。获得 !B! 点护甲。";  // 卡片描述
    private static final String DESCRIPTION_UPGRADE = "造成 !D! 点伤害两次。获得 !B! 点护甲，升级后伤害和护甲增加。";  // 升级后的描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 稀有度：普通
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡片池

    private static final int COST = 1;  // 卡片能量消耗
    private static final int DAMAGE = 4;  // 卡片的基础伤害
    private static final int DAMAGE_UPGRADED_BONUS = 2;  // 升级后的伤害加成
    private static final int BLOCK = 4;  // 卡片的基础护甲
    private static final int BLOCK_UPGRADED_BONUS = 2;  // 升级后的护甲加成

    public MagicFang() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = DAMAGE;  // 设置基础伤害
        this.baseBlock = BLOCK;  // 设置基础护甲
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用卡片时，先获得护甲
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        // 对敌人造成两次伤害，分别使用竖直和水平斩击效果
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_VERTICAL));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new MagicFang();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(DAMAGE_UPGRADED_BONUS);  // 升级时增加伤害
            upgradeBlock(BLOCK_UPGRADED_BONUS);  // 升级时增加护甲
            rawDescription = DESCRIPTION_UPGRADE;  // 更新卡片描述
            initializeDescription();  // 初始化描述
        }
    }
}
