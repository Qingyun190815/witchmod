package witchmod.actions;

import java.util.ArrayList;
import java.util.List;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class ReduceRandomDebuffAction extends AbstractGameAction {

	private String powerID;  // 存储被选中的 debuff power ID

	// 构造函数：初始化动作，设置目标、来源、减少的数量，以及随机选择的 debuff 类型
	public ReduceRandomDebuffAction(AbstractCreature target, AbstractCreature source, int amount) {
		this.setValues(target, source, amount);  // 设置目标和来源，以及减少的数量
		this.duration = Settings.ACTION_DUR_FAST;  // 设置为快速动作，持续时间为很短
		this.powerID = getRandomDebuff(target);  // 获取目标的随机 debuff 类型
		this.actionType = AbstractGameAction.ActionType.REDUCE_POWER;  // 动作类型为减少 debuff
	}

	@Override
	public void update() {
		if (powerID == null) {  // 如果没有可减少的 debuff，则结束动作
			this.isDone = true;
			return;
		}
		if (this.duration == Settings.ACTION_DUR_FAST) {  // 快速动作，立即执行
			// 遍历目标的所有 debuff
			for (AbstractPower p : this.target.powers) {
				if (!p.ID.equals(this.powerID)) continue;  // 如果这个 debuff 不是选中的随机 debuff，则跳过
				if (this.amount < p.amount) {  // 如果减少的数量小于当前 debuff 的数量
					p.reducePower(this.amount);  // 减少 debuff 数量
					p.updateDescription();  // 更新描述
					AbstractDungeon.onModifyPower();  // 刷新游戏状态
					break;  // 执行完毕，跳出循环
				}
				// 如果减少的数量大于或等于 debuff 数量，完全移除该 debuff
				AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.target, this.source, this.powerID));
				break;  // 完成后跳出循环
			}
		}
		this.tickDuration();  // 执行并继续更新
	}

	// 获取目标的随机 debuff 类型
	private String getRandomDebuff(AbstractCreature player) {
		List<String> candidates = new ArrayList<>();
		// 如果目标拥有 Weak、Frail 或 Vulnerable 中的任何一个 debuff，加入候选列表
		if (player.hasPower(WeakPower.POWER_ID)) {
			candidates.add(WeakPower.POWER_ID);
		}
		if (player.hasPower(FrailPower.POWER_ID)) {
			candidates.add(FrailPower.POWER_ID);
		}
		if (player.hasPower(VulnerablePower.POWER_ID)) {
			candidates.add(VulnerablePower.POWER_ID);
		}
		// 随机选择一个 debuff，如果没有 debuff 则返回 null
		if (candidates.size() > 0) {
			return candidates.get((int)(Math.random() * candidates.size()));
		} else {
			return null;
		}
	}
}
