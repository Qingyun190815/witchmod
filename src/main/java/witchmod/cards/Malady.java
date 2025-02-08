package witchmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.powers.RotPower;

public class Malady extends AbstractWitchCard {
    public static final String ID = "Malady";  // 卡片ID
    public static final String NAME = "Malady";  // 卡片名称
    public static final String IMG = "cards/malady.png";  // 卡片图片路径
    public static final String DESCRIPTION = "造成 !D! 点伤害并施加 !M! 点腐烂。 NL 折弃此卡。";  // 卡片描述
    private static final String DESCRIPTION_UPGRADE = "造成 !D! 点伤害并施加 !M! 点腐烂。 NL 折弃此卡，升级后增加伤害与腐烂值。";  // 升级后的描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 稀有度：不常见
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 卡片池

    private static final int COST = 1;  // 卡片能量消耗
    private static final int DAMAGE = 9;  // 卡片的基础伤害
    private static final int DAMAGE_BONUS = 3;  // 升级后的伤害加成
    private static final int MAGIC = 2;  // 卡片的基础腐烂值
    private static final int MAGIC_BONUS = 2;  // 升级后的腐烂值加成

    public Malady() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = DAMAGE;  // 设置基础伤害
        this.magicNumber = this.baseMagicNumber = MAGIC;  // 设置基础腐烂值
        this.exhaust = true;  // 使用后卡片会被消耗
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.BLUNT_LIGHT));
        // 施加腐烂效果
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new RotPower(m, p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Malady();  // 返回卡片副本
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(DAMAGE_BONUS);  // 升级时增加伤害
            upgradeMagicNumber(MAGIC_BONUS);  // 升级时增加腐烂值
            rawDescription = DESCRIPTION_UPGRADE;  // 更新卡片描述
            initializeDescription();  // 初始化描述
        }
    }
}
