package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import witchmod.WitchMod;

/**
 * GrimVengeancePower 类表示一种增益能力，当玩家受到攻击时，会对攻击者施加 Decrepit Power。
 */
public class GrimVengeancePower extends AbstractWitchPower {
	// 完整的能力 ID，用于标识此能力
	private static final String POWER_ID = "GrimVengeancePower";

	// 能力图标的路径，用于显示此能力的图像
	public static final String IMG = "powers/grimvengeance.png";

	/**
	 * GrimVengeancePower 的构造方法。
	 * @param owner 此能力的持有者（通常为玩家）
	 * @param amount 此能力的强度，通常表示 Decrepit Power 的强度
	 */
	public GrimVengeancePower(AbstractCreature owner, int amount) {
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
	 * 当玩家被攻击时，应用 Decrepit Power 给攻击者。
	 * @param info 伤害信息，包含攻击者的信息
	 * @param damageAmount 攻击造成的伤害量
	 * @return 最终伤害量
	 */
	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		// 确保攻击者不是玩家自己，并且不是特定类型的伤害
		if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != this.owner) {
			flash();  // 闪烁效果
			// 对攻击者施加 Decrepit Power
			AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(info.owner, owner, new DecrepitPower(info.owner, amount, true), amount, true));
		}
		return damageAmount;  // 返回未修改的伤害量
	}

	/**
	 * 更新能力的描述文本。
	 * @return 能力的描述
	 */
	@Override
	public void updateDescription() {
		// 格式化描述，显示 Decrepit Power 的强度
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}
}
