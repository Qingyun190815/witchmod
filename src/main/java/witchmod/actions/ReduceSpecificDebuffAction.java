package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class ReduceSpecificDebuffAction extends AbstractGameAction {
	private String powerID;  // 存储要减少的具体 debuff 的 power ID

	// 构造函数：初始化动作，设置目标、来源、debuff 类型和减少的数量
	public ReduceSpecificDebuffAction(AbstractCreature target, AbstractCreature source, String powerID, int amount) {
		this.setValues(target, source, amount);  // 设置目标、来源和减少数量
		this.duration = Settings.ACTION_DUR_FAST;  // 设置为快速动作，持续时间为很短
		this.powerID = powerID;  // 设置要减少的 debuff 类型
		this.actionType = AbstractGameAction.ActionType.REDUCE_POWER;  // 动作类型为减少 debuff
	}

	@Override
	public void update() {
		if (powerID == null) {  // 如果没有提供 powerID，结束动作
			isDone = true;
			return;
		}
		if (duration == Settings.ACTION_DUR_FAST) {  // 快速动作，立即执行
			// 遍历目标的所有 debuff
			for (AbstractPower p : target.powers) {
				if (!p.ID.equals(powerID)) continue;  // 如果这个 debuff 不是指定的，要减少的那个，跳过
				if (amount < p.amount) {  // 如果减少的数量小于当前 debuff 的数量
					p.reducePower(amount);  // 减少 debuff 数量
					p.updateDescription();  // 更新描述
					AbstractDungeon.onModifyPower();  // 刷新游戏状态
					break;  // 执行完毕，跳出循环
				}
				// 如果减少的数量大于或等于 debuff 数量，完全移除该 debuff
				AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(target, source, powerID));
				break;  // 完成后跳出循环
			}
		}
		tickDuration();  // 执行并继续更新
	}
}
