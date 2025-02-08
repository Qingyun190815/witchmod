package witchmod.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RavenAction extends AbstractGameAction {
	private AbstractPlayer player;  // 当前玩家
	public static final String TEXT = "ravenize";  // 动作提示文本
	private ArrayList<AbstractCard> eligible = new ArrayList<>();  // 存放符合条件的卡牌
	private ArrayList<AbstractCard> oldHand;  // 保存玩家手牌的旧状态

	// 构造函数：初始化动作
	public RavenAction() {
		this.duration = Settings.ACTION_DUR_FAST;  // 设置为快速动作
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;  // 动作类型为卡牌操作
		this.player = AbstractDungeon.player;  // 获取当前玩家对象
	}

	@Override
	public void update() {
		if (duration == Settings.ACTION_DUR_FAST) {  // 快速执行
			// 遍历玩家手牌，选择符合条件的卡牌
			for (AbstractCard c : player.hand.group) {
				// 条件：卡牌可以升级，或者当前回合的费用大于0，并且卡牌是攻击、技能或力量类型
				if ((c.canUpgrade() || c.costForTurn > 0) && (c.type == CardType.ATTACK || c.type == CardType.SKILL || c.type == CardType.POWER)) {
					eligible.add(c);  // 将符合条件的卡牌添加到 eligible 列表
				}
			}

			// 如果没有符合条件的卡牌，结束动作
			if (eligible.size() == 0) {
				isDone = true;
				return;
			}

			// 如果只有一张符合条件的卡牌，直接对其执行 "ravenize"
			if (eligible.size() == 1) {
				ravenize(eligible.get(0));  // "ravenize" 该卡牌
				isDone = true;
				return;
			}

			// 保存旧的手牌状态，准备让玩家选择卡牌
			oldHand = player.hand.group;
			player.hand.group = eligible;  // 更新手牌为符合条件的卡牌

			// 打开卡牌选择界面，让玩家选择要执行 "ravenize" 的卡牌
			AbstractDungeon.handCardSelectScreen.open(TEXT, 1, false, false, false, true);
			this.tickDuration();  // 更新动作持续时间
			return;
		}

		// 如果玩家已选择卡牌
		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			// 对玩家选择的卡牌执行 "ravenize"
			for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				ravenize(c);
			}
			// 恢复玩家原本的手牌状态
			this.returnCards();
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;  // 标记卡牌已取回
			AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();  // 清空选中的卡牌
			this.isDone = true;
		}

		this.tickDuration();  // 更新动作持续时间
	}

	// 恢复玩家原本的手牌状态
	private void returnCards() {
		player.hand.group = oldHand;  // 恢复手牌
		player.hand.refreshHandLayout();  // 刷新手牌布局
	}

	// 执行 "ravenize" 操作：升级卡牌或减少费用
	private void ravenize(AbstractCard c) {
		if (c.canUpgrade()) {
			c.upgrade();  // 如果卡牌可以升级，进行升级
		}
		if (c.costForTurn > 0) {
			c.modifyCostForCombat(-1);  // 如果卡牌当前回合有费用，减少其费用
		}
	}
}
