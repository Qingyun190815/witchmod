package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import witchmod.WitchMod;

/**
 * ChosenOfTheMoonPower 类表示一个与月亮的祝福相关的增益能力。
 * 在每个回合开始时，如果玩家没有 Artifact Power（神器效果），
 * 则会给予玩家一定数量的 Artifact Power（根据能力的 "amount"）。
 */
public class ChosenOfTheMoonPower extends AbstractWitchPower {
	// 完整的能力 ID，用于标识此能力
	private static final String POWER_ID = "ChosenOfTheMoonPower";

	// 能力图标的路径，用于显示此能力的图像
	public static final String IMG = "powers/chosenofthemoon.png";

	/**
	 * ChosenOfTheMoonPower 的构造方法。
	 * @param owner 该能力的持有者（通常是玩家）
	 * @param amount 能力给予 Artifact Power 的数量
	 */
	public ChosenOfTheMoonPower(AbstractCreature owner, int amount) {
		super(POWER_ID);

		// 设置能力持有者
		this.owner = owner;

		// 设置能力的数量（影响 Artifact Power 的数量）
		this.amount = amount;

		// 更新描述文本
		this.updateDescription();

		// 设置能力的图标（加载图像）
		this.img = new Texture(WitchMod.getResourcePath(IMG));

		// 设置能力类型为 BUFF（增益）
		this.type = PowerType.BUFF;
	}

	/**
	 * 更新能力的描述文本。
	 * 根据能力的数量（amount）更新描述文本，
	 * 例如，显示“如果没有 Artifact，回合开始时获得 #bX 个 Artifact Power”。
	 */
	@Override
	public void updateDescription() {
		// 更新描述文本
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

	/**
	 * 回合开始时触发的方法。
	 * 如果玩家没有 Artifact Power（神器效果），则授予 Artifact Power。
	 */
	@Override
	public void atStartOfTurn() {
		// 检查玩家是否已经有 Artifact Power
		if (!owner.hasPower(ArtifactPower.POWER_ID)) {
			// 如果没有，则将 Artifact Power 应用到玩家身上，数量为能力的 "amount"
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new ArtifactPower(owner, amount), amount));
		}
	}
}
