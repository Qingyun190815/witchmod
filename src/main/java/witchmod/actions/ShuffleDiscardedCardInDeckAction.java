package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

/**
 * ShuffleDiscardedCardInDeckAction 动作类：
 * 该动作将玩家的某张已弃牌堆中的卡牌重新放入抽牌堆，并刷新手牌布局。
 */
public class ShuffleDiscardedCardInDeckAction extends AbstractGameAction {
	// 需要操作的卡牌
	private AbstractCard card;
	// 玩家对象
	private AbstractPlayer player;

	// 构造方法：初始化玩家、卡牌和动作的基本信息
	public ShuffleDiscardedCardInDeckAction(AbstractPlayer player, AbstractCard card) {
		this.player = player;
		this.card = card;
		// 设置动作的持续时间为快速执行
		this.duration = Settings.ACTION_DUR_FAST;
		// 动作类型为卡牌操作
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
	}

	@Override
	public void update() {
		// 如果动作持续时间为快速执行
		if (this.duration == Settings.ACTION_DUR_FAST) {
			// 从弃牌堆中移除目标卡牌
			player.discardPile.removeCard(card);
			// 将目标卡牌添加到抽牌堆的顶部
			player.discardPile.moveToDeck(card, true);
			// 刷新玩家的手牌布局（确保UI界面上的手牌和卡牌状态正确显示）
			AbstractDungeon.player.hand.refreshHandLayout();
			// 动作完成，标记为已完成
			this.isDone = true;
		}
	}
}
