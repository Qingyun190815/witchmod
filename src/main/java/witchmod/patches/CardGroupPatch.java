package witchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;  // 引入SpirePatch注解，用于修改游戏方法
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;  // 引入SpireReturn类，用于返回不同的操作控制流
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入AbstractCard类，表示所有卡牌
import com.megacrit.cardcrawl.cards.CardGroup;  // 引入CardGroup类，表示卡牌组

import witchmod.cards.AbstractWitchCard;  // 引入自定义的AbstractWitchCard卡牌类

/**
 * CardGroupPatch 类：用于修改CardGroup类中的moveToDiscardPile方法。
 * 特别处理AbstractWitchCard类型的卡片，基于特定条件将其移到牌组中而不是弃牌堆。
 */
@SpirePatch(
		cls="com.megacrit.cardcrawl.cards.CardGroup",  // 目标类为CardGroup
		method="moveToDiscardPile"  // 目标方法为moveToDiscardPile，表示卡片移至弃牌堆时
)
public class CardGroupPatch {

	/**
	 * Prefix 方法：在CardGroup的moveToDiscardPile方法执行前，检查卡片类型。
	 * 如果卡片是AbstractWitchCard，基于卡片的特性决定是否将其移到牌组而非弃牌堆。
	 * @param __instance 当前CardGroup实例
	 * @param c 当前要处理的卡片
	 * @return 返回SpireReturn.Return(null)来跳过moveToDiscardPile方法的执行，或继续执行原方法
	 */
	public static SpireReturn Prefix(CardGroup __instance, AbstractCard c) {
		// 如果当前卡片是AbstractWitchCard类型
		if (c instanceof AbstractWitchCard) {
			AbstractWitchCard witchCard = (AbstractWitchCard) c;

			// 如果卡片的reshuffleOnUse标志为true，表示该卡片使用后需要重新洗入牌组
			if (witchCard.reshuffleOnUse) {
				// 将卡片移至牌组
				__instance.moveToDeck(witchCard, true);
				// 重置reshuffleOnUse标志
				witchCard.reshuffleOnUse = false;
				// 跳过moveToDiscardPile方法的执行
				return SpireReturn.Return(null);
			}
			// 如果卡片的reshuffleOnDiscardFromHand标志为true，表示该卡片从手牌中丢弃时需要重新洗入牌组
			else if (witchCard.reshuffleOnDiscardFromHand) {
				// 将卡片移至牌组
				__instance.moveToDeck(witchCard, true);
			}
		}
		// 如果条件不满足，继续执行原始的moveToDiscardPile方法
		return SpireReturn.Continue();
	}
}
