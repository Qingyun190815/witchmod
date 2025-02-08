package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class ReduceBlockAction extends AbstractGameAction {

	// 构造方法，初始化 ReduceBlockAction 动作
	public ReduceBlockAction(AbstractCreature target, AbstractCreature source, int amount) {
		this.setValues(target, source, amount);  // 设置目标、来源和减少的数值
		this.actionType = AbstractGameAction.ActionType.SPECIAL;  // 设置为特殊动作类型
		this.duration = 0;  // 立即执行，持续时间为 0
	}

	@Override
	public void update() {
		// 确保目标有效且不在死亡状态
		if (target == null || target.isDying) {
			isDone = true;  // 如果目标无效，立即完成动作
			return;
		}
		// 从目标的护盾值中减少指定的数量
		target.loseBlock(amount);

		isDone = true;  // 动作完成，设置为 true
	}
}
