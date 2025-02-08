package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import witchmod.WitchMod;
import witchmod.actions.DrawFromDiscardPileAction;

public class IntelligencePower extends AbstractWitchPower {
	// 能力的唯一标识符
	public static final String POWER_ID = "Intelligence";

	// 能力的名称
	public static final String NAME = "Intelligence";

	// 能力描述文本数组：根据所拥有的 "amount" 数量，选择适当的描述
	public static final String[] DESCRIPTIONS = new String[]{
			"在你的回合开始时，从你的弃牌堆抽取",
			"张随机牌。",
			"张随机牌。"
	};


	// 能力图标路径
	public static final String IMG = "powers/intelligence.png";

	/**
	 * IntelligencePower 的构造方法。
	 * @param owner 此能力的持有者
	 * @param amount 表示在回合开始时，从弃牌堆中随机抽取的卡牌数量
	 */
	public IntelligencePower(AbstractCreature owner, int amount) {
		super(POWER_ID);  // 调用父类构造方法设置能力 ID

		// 设置能力的持有者为传入的 owner（目标）
		this.owner = owner;

		// 设置能力图标
		this.img = new Texture(WitchMod.getResourcePath(IMG));

		// 设置该能力为增益效果（BUFF）
		this.type = PowerType.BUFF;

		// 设置能力的强度（amount），即每回合从弃牌堆抽取的卡牌数量
		this.amount = amount;

		// 更新能力的描述
		this.updateDescription();
	}

	/**
	 * 更新能力的描述文本。
	 * 根据能力的强度（amount），更新描述的文本。
	 */
	@Override
	public void updateDescription() {
		// 如果 "amount" 为 1，使用单数形式
		if (amount == 1) {
			description = DESCRIPTIONS[0] + " 1 " + DESCRIPTIONS[1];
		} else {  // 否则，使用复数形式
			description = DESCRIPTIONS[0] + " "+amount+" " + DESCRIPTIONS[2];
		}
	}

	/**
	 * 在回合开始时触发此能力的效果，从弃牌堆中抽取指定数量的卡牌。
	 */
	@Override
	public void atStartOfTurn() {
		// 添加一个动作，从弃牌堆中抽取指定数量的卡牌
		AbstractDungeon.actionManager.addToBottom(new DrawFromDiscardPileAction(amount, true, false));
	}
}
