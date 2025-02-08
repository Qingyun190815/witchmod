package witchmod.cards.familiar;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import witchmod.actions.ReduceSpecificDebuffAction;
import witchmod.cards.AbstractWitchCard;

public class ToadFamiliar extends AbstractWitchCard {
	// 卡片的唯一标识符
	public static final String ID = "ToadFamiliar";

	// 卡片名称
	public static final String NAME = "蟾蜍";

	// 卡片图片路径
	public static final String IMG = "cards/frog.png";

	// 卡片描述
	public static final String DESCRIPTION = "虚无. NL 获得 !B! 格挡.将你的脆弱、虚弱和衰弱减少 !M!. NL 耗尽.";

	// 卡片稀有度
	private static final CardRarity RARITY = CardRarity.SPECIAL;

	// 卡片目标
	private static final CardTarget TARGET = CardTarget.SELF;

	// 卡片类型
	private static final CardType TYPE = CardType.SKILL;

	// 池大小
	private static final int POOL = 0;

	// 卡片费用
	private static final int COST = 1;

	// 初始防御值
	private static final int POWER = 4;

	// 升级时增加的防御值
	private static final int UPGRADE_BONUS = 2;

	// 初始减少的 debuff 数量
	private static final int MAGIC = 1;

	// 升级时减少的 debuff 数量
	private static final int UPGRADE_MAGIC_BONUS = 1;

	public ToadFamiliar() {
		// 调用父类构造器，设置卡片ID、图片、费用、类型、稀有度和目标
		super(ID, IMG, COST, TYPE, RARITY, TARGET);

		// 设置魔法数量
		this.magicNumber = this.baseMagicNumber = MAGIC;

		// 设置防御值
		this.baseBlock = POWER;

		// 设置卡片为虚无（无法被弃牌或打击）
		this.exhaust = true;
		this.isEthereal = true;
	}

	// 使用卡片时的效果
	public void use(AbstractPlayer p, AbstractMonster m) {
		// 给玩家获得防御值
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));

		// 减少玩家身上的虚弱、脆弱和疲惫效果
		AbstractDungeon.actionManager.addToBottom(new ReduceSpecificDebuffAction(p, p, WeakPower.POWER_ID, magicNumber));
		AbstractDungeon.actionManager.addToBottom(new ReduceSpecificDebuffAction(p, p, VulnerablePower.POWER_ID, magicNumber));
		AbstractDungeon.actionManager.addToBottom(new ReduceSpecificDebuffAction(p, p, FrailPower.POWER_ID, magicNumber));
	}

	// 克隆当前卡片（用于卡片的复制功能）
	public AbstractCard makeCopy() {
		return new ToadFamiliar();
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
			upgradeBlock(UPGRADE_BONUS);  // 升级时增加防御值
			upgradeMagicNumber(UPGRADE_MAGIC_BONUS);  // 升级时增加减少 debuff 的数量
		}
	}
}
