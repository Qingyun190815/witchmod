package witchmod.cards.familiar;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction; // 导入应用力量的动作类
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction; // 导入消耗所有虚无卡片的动作类
import com.megacrit.cardcrawl.cards.AbstractCard; // 导入卡片的基础类
import com.megacrit.cardcrawl.characters.AbstractPlayer; // 导入玩家类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon; // 导入地下城类
import com.megacrit.cardcrawl.monsters.AbstractMonster; // 导入怪物类
import com.megacrit.cardcrawl.powers.ArtifactPower; // 导入Artifact力量类
import com.megacrit.cardcrawl.powers.GainStrengthPower; // 导入增加力量的力量类
import com.megacrit.cardcrawl.powers.StrengthPower; // 导入力量的力量类
import com.megacrit.cardcrawl.powers.WeakPower; // 导入虚弱的力量类

import witchmod.cards.AbstractWitchCard; // 导入自定义的巫师卡片类

public class BatFamiliar extends AbstractWitchCard { // 蝙蝠熟悉物卡片类继承自AbstractWitchCard
	public static final String ID = "BatFamiliar"; // 蝙蝠卡片的ID
	public static final String NAME = "蝙蝠"; // 蝙蝠的名称
	public static final String IMG = "cards/bat.png"; // 卡片的图像路径
	public static final String DESCRIPTION = "虚无. NL 使所有敌人获得 1 点虚弱.敌人在本回合失去 !M! 点力量. NL 弃置."; // 卡片描述

	private static final CardRarity RARITY = CardRarity.SPECIAL; // 卡片的稀有度
	private static final CardTarget TARGET = CardTarget.ENEMY; // 目标是敌人
	private static final CardType TYPE = CardType.SKILL; // 卡片类型为技能

	private static final int POOL = 0; // 池（卡片的起始数量，这里为0）

	private static final int COST = 1; // 卡片的费用
	private static final int POWER = 2; // 卡片的初始魔法值（虚弱和力量减少的值）
	private static final int UPGRADE_BONUS = 1; // 升级后的加成

	public BatFamiliar() {
		super(ID, IMG, COST, TYPE, RARITY, TARGET); // 调用父类构造函数
		this.magicNumber = this.baseMagicNumber = POWER; // 设置初始魔法数值
		this.exhaust = true; // 设置该卡片在使用后会被消耗
		this.isEthereal = true; // 设置卡片为虚无卡片，意味着它将在回合结束后消失
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		// 使用卡片时的逻辑
		// 对目标敌人施加虚弱1层
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, 1, false), 1));
		// 对目标敌人减少力量
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new StrengthPower(m, -magicNumber), -magicNumber));

		// 如果敌人没有Artifact力量或其Artifact力量小于2，则对敌人增加力量
		if (!m.hasPower(ArtifactPower.POWER_ID) || m.getPower(ArtifactPower.POWER_ID).amount < 2) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new GainStrengthPower(m, magicNumber), magicNumber));
		}
	}

	@Override
	public AbstractCard makeCopy() {
		// 返回一个新的蝙蝠熟悉物卡片副本
		return new BatFamiliar();
	}

	@Override
	public void triggerOnEndOfPlayerTurn() {
		// 在玩家回合结束时，执行消耗所有虚无卡片的动作
		AbstractDungeon.actionManager.addToTop(new ExhaustAllEtherealAction());
	}

	@Override
	public void upgrade() {
		// 升级卡片，如果卡片未被升级
		if (!upgraded) {
			upgradeName(); // 升级卡片名称
			upgradeMagicNumber(UPGRADE_BONUS); // 升级魔法数值
		}
	}
}
