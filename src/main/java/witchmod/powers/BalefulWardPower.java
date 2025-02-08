package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import witchmod.WitchMod;

/**
 * BalefulWardPower 类表示一个特殊的增益效果，该效果与玩家的 Block（防御）相关。
 * 当 Block 被破坏时，将向手牌中添加一个副本的卡片。
 */
public class BalefulWardPower extends AbstractWitchPower {
	// 完整的能力 ID，用于标识此能力
	private static final String POWER_ID = "BalefulWardPower";

	// 能力图标的路径，用于显示此能力的图像
	public static final String IMG = "powers/balefulward.png";

	// 要添加到手牌中的卡片对象
	private AbstractCard card;

	/**
	 * BalefulWardPower 的构造方法。
	 * @param cardToCopy 需要在 Block 被破坏时添加到手牌的卡片
	 */
	public BalefulWardPower(AbstractCard cardToCopy) {
		super(POWER_ID);

		// 设置能力持有者为玩家
		this.owner = AbstractDungeon.player;

		// 设置该能力所关联的卡片
		this.card = cardToCopy;

		// 设置能力的图标（加载图像）
		this.img = new Texture(WitchMod.getResourcePath(IMG));

		// 设置能力类型为 BUFF（增益）
		this.type = PowerType.BUFF;

		// 设置该能力为基于回合的增益
		this.isTurnBased = true;

		// 设置能力的数量为 1
		this.amount = 1;

		// 更新描述文本
		this.updateDescription();
	}

	/**
	 * 更新能力的描述文本。
	 * 根据能力的数量（amount）更新描述文本，
	 * 例如，显示“Block 被破坏时，添加 #bX 个 Baleful Ward 到手牌”。
	 */
	@Override
	public void updateDescription() {
		// 更新描述文本
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

	/**
	 * 当玩家的 Block 被破坏时，触发此方法。
	 * 该方法会向玩家手牌中添加副本卡片并移除该能力。
	 */
	@Override
	public void onBlockBreak() {
		// 闪烁能力图标
		flash();

		// 添加副本卡片到手牌中
		AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(card.makeStatEquivalentCopy(), this.amount));

		// 移除该能力
		AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, ID));
	}

	/**
	 * 回合结束时，移除该能力。
	 * 这确保了能力在回合结束时不会持续存在。
	 */
	@Override
	public void atEndOfRound() {
		// 在回合结束时移除该能力
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
	}
}
