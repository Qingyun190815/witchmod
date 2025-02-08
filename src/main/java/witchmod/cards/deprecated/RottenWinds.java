package witchmod.cards.deprecated;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;

import witchmod.cards.AbstractWitchCard;
import witchmod.powers.RotPower;

public class RottenWinds extends AbstractWitchCard {
	// 卡片的唯一标识符
	public static final String ID = "RottenWinds";

	// 卡片名称
	public static final String NAME = "腐烂之风";

	// 卡片图片路径
	public static final String IMG = "cards/placeholder_attack.png";

	// 卡片描述
	public static final String DESCRIPTION = "对所有敌人造成 !D! 点伤害并施加 !M! 点腐烂效果，效果持续 2 次.";

	// 卡片稀有度
	private static final CardRarity RARITY = CardRarity.UNCOMMON;

	// 卡片目标
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	// 卡片类型
	private static final CardType TYPE = CardType.ATTACK;

	// 池大小
	private static final int POOL = 1;

	// 卡片费用
	private static final int COST = 2;

	// 基础伤害
	private static final int POWER = 3;

	// 升级奖励
	private static final int UPGRADE_BONUS = 2;

	// 魔法数量（用于应用“Rot”效果）
	private static final int MAGIC = 1;

	public RottenWinds() {
		// 调用父类构造器，设置卡片ID、图片、费用、类型、稀有度和目标
		super(ID, IMG, COST, TYPE, RARITY, TARGET);

		// 设置基础伤害和魔法值
		this.baseDamage = POWER;
		this.magicNumber = this.baseMagicNumber = MAGIC;

		// 设置卡片为多重伤害卡
		this.isMultiDamage = true;
	}

	// 使用卡片时的效果
	public void use(AbstractPlayer p, AbstractMonster m) {
		// 播放“匕首喷射”特效
		AbstractDungeon.actionManager.addToBottom(new VFXAction(new DaggerSprayEffect(false), 0.0f));

		// 对所有敌人应用“Rot”效果
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new RotPower(mo, p, magicNumber, false), magicNumber, true));
		}

		// 对所有敌人造成伤害
		AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, multiDamage, DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));

		// 再次对所有敌人应用“Rot”效果
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, p, new RotPower(mo, p, magicNumber, false), magicNumber, true));
		}

		// 再次对所有敌人造成伤害
		AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(p, multiDamage, DamageType.NORMAL, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
	}

	// 克隆当前卡片（用于卡片的复制功能）
	public AbstractCard makeCopy() {
		return new RottenWinds();
	}

	// 升级卡片时的效果
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_BONUS);  // 升级时增加伤害
		}
	}
}
