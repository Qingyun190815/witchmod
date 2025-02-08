package witchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;  // 引入SpirePatch注解，用于修改游戏方法
import com.megacrit.cardcrawl.core.AbstractCreature;  // 引入AbstractCreature类，游戏中所有生物类的父类
import com.megacrit.cardcrawl.powers.AbstractPower;  // 引入AbstractPower类，游戏中所有力量效果的父类

import witchmod.powers.AbstractWitchPower;  // 引入自定义的AbstractWitchPower类

/**
 * BlockBreakPatch 类：用于在AbstractCreature类的brokeBlock方法中添加前置操作。
 * 当一个生物的护盾被打破时，检查是否有AbstractWitchPower类型的力量触发onBlockBreak方法。
 */
@SpirePatch(
		cls="com.megacrit.cardcrawl.core.AbstractCreature",  // 目标类为AbstractCreature
		method="brokeBlock",  // 目标方法为brokeBlock，表示护盾破碎时的方法
		paramtypez={}  // 无参数
)
public class BlockBreakPatch {

	/**
	 * 前置方法：在brokeBlock方法执行前，检查AbstractCreature的所有力量，
	 * 如果有AbstractWitchPower类型的力量，调用它的onBlockBreak方法。
	 */
	public static void Prefix(AbstractCreature __instance) {
		// 遍历该生物的所有力量
		for (AbstractPower p : __instance.powers) {
			// 如果力量是AbstractWitchPower的实例，调用它的onBlockBreak方法
			if (p instanceof AbstractWitchPower) {
				((AbstractWitchPower) p).onBlockBreak();
			}
		}
	}
}
