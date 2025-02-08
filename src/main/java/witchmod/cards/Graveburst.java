package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Graveburst extends AbstractWitchCard {
    public static final String ID = "Graveburst";  // 卡片ID
    public static final String NAME = "Graveburst";  // 卡片名称
    public static final String IMG = "cards/graveburst.png";  // 卡片图片路径
    public static final String DESCRIPTION = "保留。对所有敌人造成相当于你弃牌堆中攻击卡数量两倍的伤害。";  // 卡片描述
    public static final String[] EXTENDED_DESCRIPTION = new String[]{" ( !D! 伤害)"};  // 伤害说明

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;  // 目标：所有敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡池数量
    private static final int COST = 1;  // 初始能量消耗
    private static final int COST_UPGRADED = 0;  // 升级后能量消耗
    private static final int POWER = 2;  // 基础伤害

    public Graveburst() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
        this.isMultiDamage = true;  // 造成多重伤害
        this.retain = true;  // 保留卡片
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 使用时，对所有敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.FIRE, true));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Graveburst();  // 返回卡片副本
    }

    @Override
    public void atTurnStart() {
        // 回合开始时，卡片始终保留
        retain = true;
    }

    @Override
    public void applyPowers() {
        int count = 0;
        // 遍历弃牌堆中的卡片，计算攻击卡的数量
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c.type == CardType.ATTACK) {
                count++;
            }
        }
        // 基础伤害等于弃牌堆中攻击卡数量的两倍
        baseDamage = count * POWER;
        super.applyPowers();
        // 更新卡片描述
        rawDescription = DESCRIPTION;
        rawDescription += EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        // 更新卡片描述
        rawDescription = DESCRIPTION;
        rawDescription += EXTENDED_DESCRIPTION[0];
        initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBaseCost(COST_UPGRADED);  // 升级后减少能量消耗
        }
    }
}
