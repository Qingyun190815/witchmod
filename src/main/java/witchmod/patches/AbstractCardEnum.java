package witchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;  // 引入SpireEnum注解，用于创建枚举类型
import com.megacrit.cardcrawl.cards.AbstractCard;  // 引入AbstractCard类，卡牌类的基类

/**
 * AbstractCardEnum 类：用于为《杀戮尖塔》定义新的卡牌颜色类型。
 */
public class AbstractCardEnum {

	/**
	 * 使用 @SpireEnum 注解创建新的卡牌颜色类型 WITCH。
	 * 这个类型将作为 AbstractCard.CardColor 枚举的一部分，
	 * 使得玩家可以在卡牌系统中使用它来指定新的卡牌颜色。
	 */
	@SpireEnum
	public static AbstractCard.CardColor WITCH;
}
