package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class GhoulTouchAction extends AbstractGameAction {
	private DamageInfo info;

	// 构造方法，初始化动作的目标、伤害信息
	public GhoulTouchAction(AbstractCreature target, DamageInfo info) {
		this.info = info;
		this.setValues(target, info);  // 设置目标和伤害信息
		this.actionType = AbstractGameAction.ActionType.DAMAGE;  // 该动作是伤害类型
		this.duration = 0;  // 动作即时完成
	}

	@Override
	public void update() {
		// 如果目标为空或目标已死亡，结束动作
		if (target == null || target.isDying) {
			isDone = true;
			return;
		}

		// 计算未被阻挡的伤害
		int effect = getUnblockedDamage();

		// 如果伤害大于0，应用力量效果
		if (effect > 0) {
			// 对目标减少力量（负强度）
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new StrengthPower(target, -effect), -effect));

			// 如果目标没有 "Artifact" 效果，则增加力量
			if (!target.hasPower("Artifact")) {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new GainStrengthPower(target, effect), effect));
			}
		}

		// 对目标造成伤害
		target.damage(info);

		// 动作完成
		isDone = true;
	}

	// 计算未被阻挡的伤害
	private int getUnblockedDamage() {
		int baseDamage = info.output;  // 获取基础伤害
		baseDamage -= target.currentBlock;  // 减去目标的当前护盾
		if (baseDamage > 0) {
			return baseDamage;  // 如果有剩余伤害，返回它
		} else {
			return 0;  // 否则返回0
		}
	}
}
