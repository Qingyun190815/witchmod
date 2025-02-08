package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import witchmod.WitchMod;

/**
 * DarkProcessionPower 类表示一种增益能力，能在回合开始时将上回合最后一张使用的卡片复制并加入到玩家手中。
 */
public class DarkProcessionPower extends AbstractWitchPower {
	// 完整的能力 ID，用于标识此能力
	private static final String POWER_ID = "DarkProcessionPower";

	// 能力图标的路径，用于显示此能力的图像
	public static final String IMG = "powers/darkprocession.png";

	// 用于记录最后一次使用的卡片
	private AbstractCard card;

	/**
	 * DarkProcessionPower 的构造方法。
	 * @param amount 每次复制的卡片数量
	 */
	public DarkProcessionPower(int amount) {
		super(POWER_ID);

		// 设置能力持有者为玩家
		this.owner = AbstractDungeon.player;

		// 设置能力的数量（影响复制的卡片数量）
		this.amount = amount;

		// 更新能力描述文本
		this.updateDescription();

		// 设置能力的图标
		this.img = new Texture(WitchMod.getResourcePath(IMG));

		// 设置能力类型为 BUFF（增益）
		this.type = PowerType.BUFF;
	}

	/**
	 * 更新能力的描述文本。
	 * 描述文本基于复制的卡片数量（amount）和最后一次使用的卡片来生成。
	 */
	@Override
	public void updateDescription() {
		String baseDescription;

		// 根据 "amount" 设置描述文本
		if (amount == 1) {
			baseDescription = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3];
		} else {
			baseDescription = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
		}

		// 如果存在已使用的卡片，则将卡片的名字添加到描述中
		if (card != null) {
			description = baseDescription + DESCRIPTIONS[4] + card.name;
		} else {
			description = baseDescription;
		}
	}

	/**
	 * 在回合开始时触发的方法。
	 * 如果当前没有怪物或最后使用的卡片为 null，则不进行任何操作。
	 * 否则，将复制最后一张使用的卡片并将其加入玩家手中。
	 */
	@Override
	public void atStartOfTurn() {
		// 如果怪物已经基本死亡或没有最后使用的卡片，则跳过
		if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead() && card != null) {
			flash();

			// 创建一张复制的卡片
			AbstractCard toCreate = card.makeStatEquivalentCopy();

			// 将复制的卡片加入手中
			AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(toCreate, amount, false));

			// 触发卡片被抽到时的效果
			toCreate.triggerWhenDrawn();
		}
	}

	/**
	 * 在卡片被使用后触发的方法。
	 * 更新描述并记录最后使用的卡片。
	 * @param usedCard 被使用的卡片
	 */
	@Override
	public void onAfterCardPlayed(AbstractCard usedCard) {
		// 更新最后使用的卡片
		card = usedCard;

		// 更新描述文本
		updateDescription();
	}
}
