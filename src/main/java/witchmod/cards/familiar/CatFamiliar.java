package witchmod.cards.familiar;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import witchmod.cards.AbstractWitchCard;

public class CatFamiliar extends AbstractWitchCard {
	// 卡片ID，唯一标识
	public static final String ID = "CatFamiliar";
	// 卡片名称
	public static final String NAME = "猫";
	// 卡片图片路径
	public static final String IMG = "cards/cat.png";
	// 卡片描述
	public static final String DESCRIPTION = "虚无. NL 造成 !D! 点伤害 !M! 次. NL 弃置.";

	// 卡片稀有度（特殊卡）
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	// 目标（敌人）
	private static final CardTarget TARGET = CardTarget.ENEMY;
	// 卡片类型（攻击）
	private static final CardType TYPE = CardType.ATTACK;

	// 卡片池
	private static final int POOL = 0;

	// 卡片费用
	private static final int COST = 1;
	// 基础伤害
	private static final int POWER = 3;
	// 魔法数（攻击次数）
	private static final int MAGIC = 2;
	// 升级后的魔法数加成
	private static final int UPGRADE_MAGIC_BONUS = 1;

	// 构造函数
	public CatFamiliar() {
		// 初始化卡片
		super(ID, IMG, COST, TYPE, RARITY, TARGET);
		this.baseDamage = POWER;  // 设置基础伤害
		this.magicNumber = this.baseMagicNumber = MAGIC;  // 设置魔法数
		this.exhaust = true;  // 卡片在使用后会弃置
		this.isEthereal = true;  // 卡片是虚无的（即使用后立即消耗）
	}

	// 使用卡片时的效果
	public void use(AbstractPlayer p, AbstractMonster m) {
		// 对目标造成伤害
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
		if (upgraded) {
			// 如果卡片已升级，额外攻击一次
			AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		}
	}

	// 创建卡片副本
	public AbstractCard makeCopy() {
		return new CatFamiliar();
	}

	// 在玩家回合结束时触发的效果
	@Override
	public void triggerOnEndOfPlayerTurn() {
		// 弃置所有虚无卡片
		AbstractDungeon.actionManager.addToTop(new ExhaustAllEtherealAction());
	}

	// 升级卡片
	public void upgrade() {
		if (!upgraded) {
			// 升级卡片名称
			upgradeName();
			// 增加魔法数
			upgradeMagicNumber(UPGRADE_MAGIC_BONUS);
		}
	}
}
