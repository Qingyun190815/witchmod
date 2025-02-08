package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class CrystalResonanceAction extends AbstractGameAction {

	// 构造方法，初始化时设置该动作的持续时间为 0（即立刻执行）
	public CrystalResonanceAction() {
		this.duration = 0.0f;
	}

	/**
	 * 更新方法，执行该动作的逻辑：
	 * 1. 遍历玩家手牌中的所有卡片。
	 * 2. 如果卡片类型是技能（Skill），则将该卡片的费用降低 1。
	 */
	@Override
	public void update() {
		// 遍历玩家手牌中的所有卡片
		for (AbstractCard c : AbstractDungeon.player.hand.group) {
			// 如果该卡片是技能类型
			if (c.type == CardType.SKILL) {
				// 将该卡片的费用减少 1
				c.setCostForTurn(c.costForTurn - 1);
			}
		}

		// 动作完成，标记为 done
		this.isDone = true;
	}
}
