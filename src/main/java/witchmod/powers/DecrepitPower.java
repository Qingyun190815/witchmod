package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import witchmod.WitchMod;
import witchmod.relics.WalkingCane;

/**
 * DecrepitPower 类表示一种负面状态的增益能力，它会在每回合结束时减少其效果，并可能增加受到的伤害。
 */
public class DecrepitPower extends AbstractWitchPower {
	// 完整的能力 ID，用于标识此能力
	private static final String POWER_ID = "Decrepit";

	// 能力图标的路径，用于显示此能力的图像
	public static final String IMG = "powers/decrepit.png";

	// 标记该能力是否刚刚应用，用于控制回合更新行为
	private boolean justApplied = false;

	/**
	 * DecrepitPower 的构造方法。
	 * @param owner 拥有该能力的角色
	 * @param amount 此能力的强度，通常影响伤害增加值
	 * @param justApplied 是否刚刚应用该能力
	 */
	public DecrepitPower(AbstractCreature owner, int amount, boolean justApplied) {
		super(POWER_ID);

		// 设置能力的持有者
		this.owner = owner;

		// 设置能力的强度（amount）
		this.amount = amount;

		// 更新能力的描述
		this.updateDescription();

		// 设置能力图标
		this.img = new Texture(WitchMod.getResourcePath(IMG));

		// 设置该能力为回合制增益（DEBUFF）
		this.isTurnBased = true;
		this.type = PowerType.DEBUFF;

		// 标记是否刚应用此能力
		this.justApplied = justApplied;
	}

	/**
	 * 更新能力的描述文本。
	 * @return 能力的描述
	 */
	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + "."; // 格式化描述，显示此能力的强度
	}

	/**
	 * 在每回合结束时触发的方法。
	 * 如果能力刚应用或玩家拥有 WalkingCane 遗物，跳过更新，否则减少该能力的强度。
	 */
	@Override
	public void atEndOfRound() {
		// 如果刚刚应用此能力，跳过本回合更新
		if (justApplied) {
			justApplied = false;
			return;
		}

		// 如果玩家拥有 WalkingCane 遗物，跳过更新
		if (AbstractDungeon.player.hasRelic(WalkingCane.ID)) {
			AbstractDungeon.player.getRelic(WalkingCane.ID).flash();
			return;
		}

		// 如果能力强度为 1 或更低，移除该能力；否则减少能力的强度
		if (amount <= 1) {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
		} else {
			AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, ID, 1));
		}
	}

	/**
	 * 在接收伤害时触发的方法。
	 * 如果伤害类型为 NORMAL，增加 amount 的伤害。
	 * @param damage 原始的伤害数值
	 * @param damageType 伤害类型
	 * @return 修改后的伤害数值
	 */
	@Override
	public float atDamageReceive(float damage, DamageType damageType) {
		// 如果是普通伤害类型，增加 amount 的伤害
		if (damageType == DamageType.NORMAL) {
			return damage + amount;
		} else {
			// 否则不改变伤害
			return damage;
		}
	}
}
