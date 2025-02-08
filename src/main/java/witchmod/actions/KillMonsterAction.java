package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class KillMonsterAction extends AbstractGameAction {
	AbstractMonster monster;  // 目标怪物

	// 构造方法，初始化 KillMonsterAction 动作
	public KillMonsterAction(AbstractMonster target) {
		this.monster = target;  // 设置目标怪物
		this.actionType = AbstractGameAction.ActionType.SPECIAL;  // 设置为特殊动作类型
		this.duration = 0;  // 立即执行，持续时间为 0
	}

	@Override
	public void update() {
		// 给目标怪物造成足够的伤害，使其死亡
		monster.damage(new DamageInfo(monster, monster.currentHealth, DamageType.HP_LOSS));
		isDone = true;  // 动作完成，设置为 true
	}
}
