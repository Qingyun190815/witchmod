package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class MercuryWand extends AbstractWitchCard {
    public static final String ID = "MercuryWand";  // 卡片ID
    public static final String NAME = "Mercury Wand";  // 卡片名称
    public static final String IMG = "cards/mercurywand.png";  // 卡片图片路径
    public static final String DESCRIPTION = "如果你没有护甲，则获得 !B! 护甲，否则造成 !D! 点伤害。";  // 卡片描述
    public static final String[] EXTENDED_DESCRIPTION = new String[]{" NL 将获得 !B! 护甲", " NL 将造成 !D! 点伤害"};  // 扩展描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 稀有度：普通
    private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;  // 目标：自己和敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡片池

    private static final int COST = 1;  // 卡片能量消耗
    private static final int DAMAGE = 9;  // 基础伤害
    private static final int BLOCK = 9;  // 基础护甲
    private static final int UPGRADE_BONUS = 4;  // 升级后的伤害和护甲加成

    public MercuryWand() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = DAMAGE;  // 设置基础伤害
        this.baseBlock = BLOCK;  // 设置基础护甲
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 如果玩家当前没有护甲
        if (p.currentBlock == 0) {
            // 获得护甲
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
        } else {
            // 否则，对敌人造成伤害
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        rawDescription = DESCRIPTION;
        // 根据玩家的护甲状态更新卡片描述和目标
        if (AbstractDungeon.player.currentBlock == 0) {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];  // 如果没有护甲，添加护甲描述
            target = CardTarget.SELF;  // 目标为自己
        } else {
            rawDescription += cardStrings.EXTENDED_DESCRIPTION[1];  // 否则，添加伤害描述
            target = CardTarget.ENEMY;  // 目标为敌人
        }
        initializeDescription();  // 初始化描述
    }

    @Override
    public void onMoveToDiscard() {
        rawDescription = DESCRIPTION;  // 恢复卡片的原始描述
        initializeDescription();  // 初始化描述
    }

    @Override
    public AbstractCard makeCopy() {
        return new MercuryWand();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级伤害
            upgradeBlock(UPGRADE_BONUS);  // 升级护甲
        }
    }
}
