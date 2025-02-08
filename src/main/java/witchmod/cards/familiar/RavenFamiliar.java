package witchmod.cards.familiar;

import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import witchmod.actions.RavenAction;
import witchmod.cards.AbstractWitchCard;

public class RavenFamiliar extends AbstractWitchCard {
	// 卡片的唯一标识符
	public static final String ID = "RavenFamiliar";

	// 卡片名称
	public static final String NAME = "乌鸦";

	// 卡片图片路径
	public static final String IMG = "cards/raven.png";

	// 卡片描述
	public static final String DESCRIPTION = "虚无. NL 升级你手中的一张牌并减少其费用 1,直到战斗结束. NL 耗尽.";

	// 卡片稀有度
	private static final CardRarity RARITY = CardRarity.SPECIAL;

	// 卡片目标
	private static final CardTarget TARGET = CardTarget.NONE;

	// 卡片类型
	private static final CardType TYPE = CardType.SKILL;

	// 池大小
	private static final int POOL = 0;

	// 卡片费用
	private static final int COST = 1;

	// 升级后的卡片费用
	private static final int COST_UPGRADED = 0;

	public RavenFamiliar() {
		// 调用父类构造器，设置卡片ID、图片、费用、类型、稀有度和目标
		super(ID, IMG, COST, TYPE, RARITY, TARGET);

		// 设置卡片为虚无（无法被弃牌或打击）
		this.exhaust = true;
		this.isEthereal = true;
	}

	// 使用卡片时的效果
	public void use(AbstractPlayer p, AbstractMonster m) {
		// 添加 RavenAction 动作，该动作用于升级手牌中的一张卡片并减少其费用
		AbstractDungeon.actionManager.addToBottom(new RavenAction());
	}

	// 克隆当前卡片（用于卡片的复制功能）
	public AbstractCard makeCopy() {
		return new RavenFamiliar();
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
			upgradeBaseCost(COST_UPGRADED);  // 升级时将卡片费用降为0
		}
	}
}
