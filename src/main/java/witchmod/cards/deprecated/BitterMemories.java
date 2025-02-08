package witchmod.cards.deprecated;

import java.util.HashMap;
import java.util.Map;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import witchmod.actions.BitterMemoriesAction;
import witchmod.cards.AbstractWitchCard;

public class BitterMemories extends AbstractWitchCard {
	// 卡片的唯一标识符
	public static final String ID = "BitterMemories";

	// 卡片名称
	public static final String NAME = "痛苦回忆";

	// 卡片图片路径
	public static final String IMG = "cards/placeholder_skill.png";

	// 卡片描述
	public static final String DESCRIPTION = "将每种不同类型中随机弃置的一张牌洗回你的抽牌堆，且每张牌为你提供 !B! 点格挡.";

	// 扩展描述（升级后的效果）
	public static final String EXTENDED_DESCRIPTION[] = {" NL You will gain ", " block."};

	// 卡片稀有度
	private static final CardRarity RARITY = CardRarity.UNCOMMON;

	// 卡片目标
	private static final CardTarget TARGET = CardTarget.SELF;

	// 卡片类型
	private static final CardType TYPE = CardType.SKILL;

	// 池大小
	private static final int POOL = 1;

	// 卡片费用
	private static final int COST = 1;

	// 基础防御力
	private static final int POWER = 3;

	// 升级后的防御力
	private static final int POWER_UPGRADED = 4;

	public BitterMemories() {
		// 调用父类构造器，设置卡片ID、图片、费用、类型、稀有度和目标
		super(ID, IMG, COST, TYPE, RARITY, TARGET);

		// 设置基础防御力
		this.baseBlock = POWER;
	}

	// 使用卡片时的效果
	public void use(AbstractPlayer p, AbstractMonster m) {
		// 将BitterMemoriesAction添加到游戏中的动作队列，触发卡片的效果
		AbstractDungeon.actionManager.addToBottom(new BitterMemoriesAction(block));
	}

	// 克隆当前卡片（用于卡片的复制功能）
	public AbstractCard makeCopy() {
		return new BitterMemories();
	}

	// 计算弃牌堆中不同卡片类型的数量
	private int countCardTypes() {
		Map<CardType, Boolean> types = new HashMap<>();
		for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
			// 如果弃牌堆中有不同类型的卡片，则加入到types集合中
			if (!types.containsKey(c.type)) {
				types.put(c.type, true);
			}
		}
		return types.size();
	}

	// 应用卡片效果时的描述更新
	@Override
	public void applyPowers() {
		super.applyPowers();
		// 设置卡片描述
		rawDescription = DESCRIPTION;
		rawDescription += EXTENDED_DESCRIPTION[0] + (block * countCardTypes() + EXTENDED_DESCRIPTION[1]);
		// 更新卡片的描述
		this.initializeDescription();
	}

	// 升级卡片时的效果
	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeBlock(POWER_UPGRADED);
		}
	}
}
