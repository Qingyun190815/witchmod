package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.Settings;

/**
 * ShroomsAction 动作类：
 * 此动作的主要功能是改变玩家抽牌堆顶卡牌的费用为一个随机值（0~3之间），
 * 然后立即抽一张卡。该动作常与“Shrooms”相关联，用于模拟一种随机成本变化的效果。
 */
public class ShroomsAction extends AbstractGameAction {

	// 构造方法：初始化动作参数
	public ShroomsAction() {
		// 动作持续时间设置为0，表示此动作将立即执行
		this.duration = 0.0f;
		// 设置动作类型为卡牌操作
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
	}

	@Override
	public void update() {
		// 如果玩家的抽牌堆和弃牌堆总数为0，则没有卡牌可操作，动作结束
		if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() == 0) {
			this.isDone = true;
			return;
		}

		// 如果抽牌堆为空，则需要先洗牌
		if (AbstractDungeon.player.drawPile.isEmpty()) {
			// 将此动作重新加入到动作队列，以便在洗牌后再次执行
			AbstractDungeon.actionManager.addToTop(new ShroomsAction());
			// 洗牌动作：将弃牌堆洗入抽牌堆
			AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
			// 动作结束
			this.isDone = true;
			return;
		}

		// 从抽牌堆顶获取一张卡牌
		AbstractCard c = AbstractDungeon.player.drawPile.getTopCard();

		// 生成一个随机的新费用，范围在0到3之间（含0与3）
		int newCost = AbstractDungeon.cardRandomRng.random(3);

		/*
		 * 检查该卡牌是否满足修改费用的条件：
		 * 1. 卡牌费用大于 -1（-1通常表示无法使用或特殊卡牌），
		 * 2. 卡牌颜色不为 CURSE（诅咒卡不被修改），
		 * 3. 卡牌类型不为 STATUS（状态卡不被修改），
		 * 4. 当前回合费用与随机生成的新费用不同。
		 */
		if (c.cost > -1 && c.color != AbstractCard.CardColor.CURSE
				&& c.type != AbstractCard.CardType.STATUS && c.cost != newCost) {
			// 将卡牌的本回合费用设为随机生成的新费用
			c.costForTurn = newCost;
			// 标记该卡牌本回合费用已被修改（用于界面显示）
			c.isCostModifiedForTurn = true;
		}

		// 在修改费用后，为玩家添加一个抽一张卡的动作
		AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, 1));

		// 动作完成，标记为结束
		this.isDone = true;
	}
}
