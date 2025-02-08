package witchmod.cards.familiar;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import witchmod.cards.AbstractWitchCard;
import witchmod.powers.DecrepitPower;

public class RatFamiliar extends AbstractWitchCard {
	// 卡片的唯一标识符
	public static final String ID = "RatFamiliar";

	// 卡片名称
	public static final String NAME = "老鼠";

	// 卡片图片路径
	public static final String IMG = "cards/rat.png";

	// 卡片描述
	public static final String DESCRIPTION = "虚无. NL 施加 !M! 中毒和 !M! witchmod:decrepit. NL 耗尽.";

	// 卡片稀有度
	private static final CardRarity RARITY = CardRarity.SPECIAL;

	// 卡片目标
	private static final CardTarget TARGET = CardTarget.ENEMY;

	// 卡片类型
	private static final CardType TYPE = CardType.SKILL;

	// 池大小
	private static final int POOL = 0;

	// 卡片费用
	private static final int COST = 1;

	// 魔法数量（用于施加毒素和虚弱）
	private static final int POWER = 2;

	// 升级奖励
	private static final int UPGRADE_BONUS = 1;

	public RatFamiliar() {
		// 调用父类构造器，设置卡片ID、图片、费用、类型、稀有度和目标
		super(ID, IMG, COST, TYPE, RARITY, TARGET);

		// 设置魔法数量
		this.magicNumber = this.baseMagicNumber = POWER;

		// 设置卡片为虚无（无法被弃牌或打击）
		this.exhaust = true;
		this.isEthereal = true;
	}

	// 使用卡片时的效果
	public void use(AbstractPlayer p, AbstractMonster m) {
		// 对敌人施加毒素效果
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new PoisonPower(m, p, magicNumber), magicNumber));

		// 对敌人施加虚弱效果
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new DecrepitPower(m, magicNumber, false), magicNumber));
	}

	// 克隆当前卡片（用于卡片的复制功能）
	public AbstractCard makeCopy() {
		return new RatFamiliar();
	}

	// 在玩家回合结束时触发，将所有虚无卡片从手牌中移除
	@Override
	public void triggerOnEndOfPlayerTurn() {
		AbstractDungeon.actionManager.addToTop(new ExhaustAllEtherealAction());
	}

	// 升级卡片时的效果
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);  // 升级时增加魔法数量
		}
	}
}
