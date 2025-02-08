package witchmod.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class BitterMemoriesAction extends AbstractGameAction {
	/**
	 * 构造方法，用于创建一个 BitterMemoriesAction 动作
	 * @param amount 获得的护甲值
	 */
	public BitterMemoriesAction(int amount) {
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;  // 设置动作类型为卡牌操作
		this.duration = 0;  // 设置动作的持续时间
		this.amount = amount;  // 设置获得的护甲值
	}

	/**
	 * 执行动作的方法
	 * 1. 遍历玩家的弃牌堆，将卡牌按类型分组
	 * 2. 从每种卡牌类型中随机选择一张卡牌
	 * 3. 将这些卡牌重新洗回卡组，并为玩家提供护甲
	 */
	@Override
	public void update() {
		// 创建一个 Map，将卡牌类型与卡牌列表关联
		Map<CardType, List<AbstractCard>> types = new HashMap<>();

		// 遍历玩家的弃牌堆，将卡牌按类型分组
		for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
			List<AbstractCard> list;

			// 如果该类型已经存在于 Map 中，则将卡牌加入相应的列表
			if (types.containsKey(c.type)) {
				list = types.get(c.type);
			} else {
				// 如果该类型不存在，则创建新的列表
				list = new ArrayList<>();
			}
			list.add(c);

			// 将列表添加到 Map 中
			types.put(c.type, list);
		}

		// 创建一个新的卡牌列表，用于存储从每种类型中随机选出的卡牌
		List<AbstractCard> cards = new ArrayList<>();

		// 遍历 Map，从每种卡牌类型中随机选择一张卡牌
		for (Entry<CardType, List<AbstractCard>> entry : types.entrySet()) {
			cards.add(getRandomCardFromList(entry.getValue()));
		}

		// 对选出的卡牌进行操作
		for (AbstractCard card : cards) {
			// 将选出的卡牌重新洗回卡组
			AbstractDungeon.actionManager.addToBottom(new ShuffleDiscardedCardInDeckAction(AbstractDungeon.player, card));

			// 给玩家提供护甲
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, amount));
		}

		// 动作执行完毕
		this.isDone = true;
	}

	/**
	 * 从卡牌列表中随机选择一张卡牌
	 * @param list 卡牌列表
	 * @return 随机选择的卡牌
	 */
	private AbstractCard getRandomCardFromList(List<AbstractCard> list) {
		return list.get(AbstractDungeon.cardRng.random(list.size() - 1));  // 随机返回一张卡牌
	}
}
