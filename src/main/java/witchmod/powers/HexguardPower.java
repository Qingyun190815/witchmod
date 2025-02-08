package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import witchmod.WitchMod;

/**
 * HexguardPower 类表示一种临时获得 Artifact 能力的增益。
 * 玩家获得该能力时，暂时增加 Artifact Power，直到回合结束。
 */
public class HexguardPower extends AbstractWitchPower {
	// 完整的能力 ID，用于标识此能力
	private static final String POWER_ID = "HexguardPower";

	// 能力图标的路径，用于显示此能力的图像
	public static final String IMG = "powers/hexguard.png";

	/**
	 * HexguardPower 的构造方法。
	 * @param owner 此能力的持有者（通常为玩家）
	 * @param amount 提供的 Artifact 强度
	 */
	public HexguardPower(AbstractCreature owner, int amount) {
		super(POWER_ID);  // 调用父类构造方法设置能力 ID

		// 设置能力的持有者为传入的 owner（玩家或其他角色）
		this.owner = owner;

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
	 */
	@Override
	public void updateDescription() {
		// 格式化描述，显示 Artifact 增益的强度
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

	/**
	 * 在能力首次应用时，应用 Artifact Power 给玩家。
	 * 这将为玩家提供一段时间的 Artifact 强度。
	 */
	@Override
	public void onInitialApplication() {
		// 在首次应用时，向玩家施加 Artifact Power
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new ArtifactPower(owner, amount), amount));
	}

	/**
	 * 在每回合开始时，减少玩家的 Artifact 强度，并移除 HexguardPower。
	 * 这代表 Artifact Power 在本回合结束后消失。
	 */
	@Override
	public void atStartOfTurn() {
		flash();  // 闪烁效果

		// 减少玩家身上的 Artifact Power 强度
		AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, ArtifactPower.POWER_ID, amount));

		// 移除 HexguardPower
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
	}
}
