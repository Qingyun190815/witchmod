package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PoisonPower;

import witchmod.powers.RotPower;

public class CorruptBloodAction extends AbstractGameAction {

	/**
	 * 构造方法，用于初始化 CorruptBloodAction
	 * @param target 目标生物（敌人）
	 * @param source 动作来源（通常是玩家）
	 * @param amount 中毒或腐烂效果的堆叠数量
	 */
	public CorruptBloodAction(AbstractCreature target, AbstractCreature source, int amount) {
		this.setValues(target, source, amount);  // 设置目标、来源和数量
		this.actionType = AbstractGameAction.ActionType.DEBUFF;  // 设置动作类型为负面效果
		this.duration = 0;  // 动作立即执行
	}

	/**
	 * 执行该动作的方法：
	 * 1. 如果目标已经有中毒效果，将中毒效果转换为腐烂效果。
	 * 2. 如果目标没有中毒效果，则直接施加中毒效果。
	 */
	@Override
	public void update() {
		// 检查目标是否已有中毒效果
		if (target.hasPower(PoisonPower.POWER_ID)) {
			// 获取目标的中毒堆叠数
			int poisonStacks = target.getPower(PoisonPower.POWER_ID).amount;

			// 移除目标的中毒效果
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, PoisonPower.POWER_ID));

			// 将中毒堆叠转换为腐烂效果（Rot），并应用
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new RotPower(target, source, poisonStacks), poisonStacks, true));
		} else {
			// 如果目标没有中毒效果，直接施加中毒效果
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new PoisonPower(target, source, amount), amount, true));
		}

		// 动作完成
		isDone = true;
	}
}
