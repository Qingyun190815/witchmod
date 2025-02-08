package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.ExhaustBlurEffect;
import com.megacrit.cardcrawl.vfx.ExhaustEmberEffect;

import witchmod.cards.AbstractWitchCleansableCurse;

public class CleanseAction extends AbstractGameAction{
	private AbstractWitchCleansableCurse card;  // 被净化的卡片

	public CleanseAction(AbstractWitchCleansableCurse card) {
		this.card = card;  // 初始化卡片
		this.duration = Settings.ACTION_DUR_FAST;  // 设置动作的持续时间为快速
		this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;  // 设置动作类型为卡片操作
	}

	@Override
	public void update() {
		if (duration == Settings.ACTION_DUR_FAST) {
			int i;

			// 播放净化的音效
			CardCrawlGame.sound.play("CARD_EXHAUST", 0.2f);

			// 创建视觉效果：耗尽模糊效果
			for (i = 0; i < 90; ++i) {
				AbstractDungeon.effectsQueue.add(new ExhaustBlurEffect(card.current_x, card.current_y));
			}

			// 创建视觉效果：耗尽的火焰效果
			for (i = 0; i < 50; ++i) {
				AbstractDungeon.effectsQueue.add(new ExhaustEmberEffect(card.current_x, card.current_y));
			}
		}
		tickDuration();  // 更新持续时间
		if (isDone) {  // 如果动作完成
			card.cleanse();  // 执行卡片净化
		}
	}
}
