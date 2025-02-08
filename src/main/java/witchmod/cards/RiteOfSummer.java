package witchmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import witchmod.actions.RiteOfSummerAction;

public class RiteOfSummer extends AbstractWitchCard {
    public static final String ID = "RiteOfSummer";  // 卡片ID
    public static final String NAME = "Rite of Summer";  // 卡片名称
    public static final String IMG = "cards/riteofsummer.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Discard any number of cards and deal !D! damage to a random enemy for each card discarded.";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.UNCOMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.ATTACK;  // 卡片类型：攻击

    private static final int POOL = 1;  // 池中的卡片数量
    private static final int COST = 2;  // 能量消耗

    private static final int POWER = 6;  // 初始伤害
    private static final int UPGRADE_BONUS = 3;  // 升级后伤害加成

    public RiteOfSummer() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseDamage = POWER;  // 设置基础伤害
    }

    // 使用卡片时的逻辑
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new RiteOfSummerAction(new DamageInfo(p, baseDamage, damageTypeForTurn)));  // 执行 RiteOfSummerAction（自定义行为）
    }

    // 创建卡片副本
    public AbstractCard makeCopy() {
        return new RiteOfSummer();  // 返回副本
    }

    // 升级时的行为
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级伤害
        }
    }
}
