package witchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import witchmod.relics.WalkingCane;

import java.util.ArrayList;

@SpirePatch(cls="com.megacrit.cardcrawl.powers.WeakPower", method="atEndOfRound")
public class WeakPowerPatch {

	@SpireInsertPatch(locator=Locator.class)
	public static SpireReturn<Void> Insert(WeakPower __instance) {
		// 检查玩家是否拥有 WalkingCane 遗物，并且该 Power 不是来自玩家
		if (__instance.owner != AbstractDungeon.player && AbstractDungeon.player.hasRelic(WalkingCane.ID)) {
			// 如果条件满足，触发 WalkingCane 的闪烁效果
			AbstractDungeon.player.getRelic(WalkingCane.ID).flash();
			return SpireReturn.Return(null); // 阻止 WeakPower 的默认行为
		}
		return SpireReturn.Continue(); // 否则继续执行原本的逻辑
	}

	// 使用 Locator 来查找 WeakPower 中的特定行
	private static class Locator extends SpireInsertLocator {
		public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
			// 查找对 WeakPower.amount 字段的访问位置
			Matcher amountCheckMatcher = new Matcher.FieldAccessMatcher(WeakPower.class, "amount");
			return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), amountCheckMatcher);
		}
	}
}
