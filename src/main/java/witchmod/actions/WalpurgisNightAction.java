package witchmod.actions;

import java.util.ArrayList;
import java.util.List;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import witchmod.cards.WalpurgisNight;

/**
 * WalpurgisNightAction 动作类：
 * 该动作从玩家的遗弃牌堆中筛选出可用的攻击或技能卡牌（非以太卡和 WalpurgisNight 卡牌），
 * 将其重新放入抽牌堆，并根据指定数量选择一些卡牌放在抽牌堆顶部，立即可供抽取。
 */
public class WalpurgisNightAction extends AbstractGameAction {

	// 构造方法：初始化动作并设置相关参数
	public WalpurgisNightAction(int amount) {
		this.amount = amount;  // 表示放置到抽牌堆顶部的卡牌数量
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;  // 动作类型为卡牌操作
		this.duration = 0.5f;  // 动作持续时间为0.5秒
	}

	@Override
	public void update() {
		// 当动作持续时间为0.5秒时执行
		if (duration == 0.5f) {
			AbstractPlayer player = AbstractDungeon.player;  // 获取玩家对象
			List<AbstractCard> cardsToReshuffle = new ArrayList<>();  // 用于存储将重新放入抽牌堆的卡牌

			// 从玩家的遗弃牌堆中筛选出符合条件的卡牌
			for (AbstractCard card : player.exhaustPile.group) {
				// 过滤掉以太卡牌、WalpurgisNight卡牌以及其他不符合条件的卡牌
				if ((card.type == CardType.ATTACK || card.type == CardType.SKILL)
						&& !card.isEthereal
						&& !card.cardID.equals(WalpurgisNight.ID)) {
					// 将符合条件的卡牌添加到重新洗牌列表
					cardsToReshuffle.add(card);
				}
			}

			// 确定要放置在抽牌堆顶部的卡牌数量，不能超过符合条件的卡牌数量
			int amountToPutOnTop = Math.min(cardsToReshuffle.size(), amount);

			// 将一些卡牌放到抽牌堆顶部，使其立即可抽
			while (amountToPutOnTop > 0) {
				// 随机选择一张卡牌
				AbstractCard card = cardsToReshuffle.remove((int)(Math.random() * cardsToReshuffle.size()));
				// 清除卡牌的所有特殊效果，如发光等
				card.stopGlowing();
				card.unhover();
				card.unfadeOut();
				// 将该卡牌放到抽牌堆顶部
				player.exhaustPile.moveToDeck(card, false);
				amountToPutOnTop--;  // 递减放置数量
			}

			// 将剩余的卡牌放入抽牌堆，并进行洗牌
			for (AbstractCard card : cardsToReshuffle) {
				card.stopGlowing();
				card.unhover();
				card.unfadeOut();
				player.exhaustPile.moveToDeck(card, true);  // 将卡牌加入抽牌堆，并进行洗牌
			}
		}
		// 更新动作的持续时间
		tickDuration();
	}
}
