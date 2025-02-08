package witchmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;

public class ForesightAction extends AbstractGameAction {

	// 构造方法，初始化动作的持续时间和类型
	public ForesightAction() {
		this.duration = 0.0f;
		this.actionType = AbstractGameAction.ActionType.WAIT;  // 该动作属于等待类型
	}

	@Override
	public void update() {
		// 如果抽牌堆和弃牌堆都为空，结束动作
		if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() == 0) {
			isDone = true;
			return;
		}

		// 如果抽牌堆为空，则执行洗牌操作并重新执行此动作
		if (AbstractDungeon.player.drawPile.isEmpty()) {
			AbstractDungeon.actionManager.addToTop(new ForesightAction());
			AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());  // 洗牌
			isDone = true;
			return;
		}

		// 从抽牌堆顶抽取一张卡片
		AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();

		// 执行抽卡动作
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));

		// 如果是诅咒卡或状态卡，则处理它
		if (card.type == CardType.CURSE || card.type == CardType.STATUS) {
			// 设置卡片的位置和动画效果，表现为卡片被移除
			card.current_y = -200.0f * Settings.scale;
			card.target_x = Settings.WIDTH / 2.0f + 200;
			card.target_y = Settings.HEIGHT / 2.0f;
			card.targetAngle = 0.0f;
			card.lighten(false);
			card.drawScale = 0.12f;
			card.targetDrawScale = 0.75f;

			// 延迟0.5秒后，执行卡片的废弃操作
			AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5f));
			AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
		} else {
			// 如果是其他类型的卡片，将其重新放回抽牌堆顶部，并添加紫色光效
			AbstractDungeon.player.drawPile.addToTop(card.makeStatEquivalentCopy());
			AbstractDungeon.actionManager.addToBottom(new VFXAction(
					AbstractDungeon.player,
					new VerticalAuraEffect(Color.PURPLE, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY),
					0.5f
			));
			// 再次抽卡
			AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
		}

		// 动作完成
		isDone = true;
	}
}
