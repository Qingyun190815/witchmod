package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.AthamePower;

public class Athame extends AbstractWitchCard {
    public static final String ID = "Athame";  // 卡片ID
    public static final String NAME = "仪式匕首";  // 卡片名称
    public static final String IMG = "cards/athame.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 !D! 点伤害.如果是致命攻击,增加获得稀有卡的机会 !M!%,并消耗此卡.";  // 描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 池（数量）

    private static final int COST = 1;  // 能量消耗
    private static final int POWER = 8;  // 初始伤害
    private static final int POWER_UPGRADED_BONUS = 3;  // 升级后增加的伤害
    private static final int MAGIC_NUMBER = 10;  // 初始增加的稀有卡片几率
    private static final int MAGIC_NUMBER_UPGRADED_BONUS = 10;  // 升级后增加的稀有卡片几率

    public Athame() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置卡片的基础伤害
        this.baseMagicNumber = this.magicNumber = MAGIC_NUMBER;  // 设置卡片的基础魔法数
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 如果伤害足够杀死敌人，则触发额外效果
        if ((damage >= m.currentHealth + m.currentBlock && damageTypeForTurn == DamageType.NORMAL) ||
                (damage >= m.currentHealth && damageTypeForTurn == DamageType.HP_LOSS)) {
            // 敌人会因这次攻击死亡
            exhaust = true;  // 使卡片消耗
            // 增加玩家获得稀有卡片的概率
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AthamePower(AbstractDungeon.player, magicNumber), magicNumber));
        }
        // 进行伤害攻击
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_VERTICAL));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Athame();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(POWER_UPGRADED_BONUS);  // 升级伤害
            upgradeMagicNumber(MAGIC_NUMBER_UPGRADED_BONUS);  // 升级魔法数（稀有卡片几率）
        }
    }
}
