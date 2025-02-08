package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import witchmod.WitchMod;

/**
 * DeliriumFormPower 类表示一种增益能力，它会在玩家打出非零费用的卡牌时，增加抽卡数量。
 */
public class DeliriumFormPower extends AbstractWitchPower {
	// 完整的能力 ID，用于标识此能力
	private static final String POWER_ID = "DeliriumFormPower";

	// 能力图标的路径，用于显示此能力的图像
	public static final String IMG = "powers/deliriumform.png";

	/**
	 * DeliriumFormPower 的构造方法。
	 * @param amount 此能力的强度，通常影响抽卡的数量
	 */
	public DeliriumFormPower(int amount) {
		super(POWER_ID);

		// 设置能力的持有者为玩家
		this.owner = AbstractDungeon.player;

		// 设置能力的强度（amount）
		this.amount = amount;

		// 更新能力的描述
		this.updateDescription();

		// 设置能力图标
		this.img = new Texture(WitchMod.getResourcePath(IMG));

		// 设置该能力为增益（BUFF）
		this.type = PowerType.BUFF;
	}

	/**
	 * 更新能力的描述文本。
	 * @return 能力的描述
	 */
	@Override
	public void updateDescription() {
		// 根据能力强度（amount）生成描述
		if (amount == 1) {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
		} else {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
		}
	}

	/**
	 * 在玩家打出卡牌后触发的方法。
	 * 如果打出的卡牌不是费用为 0 的卡牌，增加抽取卡牌的数量。
	 * @param usedCard 玩家打出的卡牌
	 */
	@Override
	public void onAfterCardPlayed(AbstractCard usedCard) {
		// 如果打出的卡牌费用不为 0，则增加抽卡
		if (usedCard.costForTurn != 0) {
			AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, amount));
		}
	}
}
