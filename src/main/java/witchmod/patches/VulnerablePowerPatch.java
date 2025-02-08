package witchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import basemod.ReflectionHacks;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import witchmod.relics.WalkingCane;

import java.util.ArrayList;

@SpirePatch(cls="com.megacrit.cardcrawl.powers.VulnerablePower", method="atEndOfRound")
public class VulnerablePowerPatch {

	@SpireInsertPatch(locator=Locator.class)
	public static SpireReturn<Void> Insert(VulnerablePower __instance) {
		// 检查是否是玩家（owner），并且玩家是否拥有 WalkingCane 遗物
		if (__instance.owner != AbstractDungeon.player && AbstractDungeon.player.hasRelic(WalkingCane.ID)) {
			// 如果玩家拥有 WalkingCane 遗物，触发遗物的效果（例如闪烁）
			AbstractDungeon.player.getRelic(WalkingCane.ID).flash();
			// 阻止状态减弱，直接返回，表示不继续执行默认逻辑
			return SpireReturn.Return(null);
		}
		// 默认行为：继续执行原有的状态结束逻辑
		return SpireReturn.Continue();
	}

	// Locator 类用于查找代码插入的位置
	private static class Locator extends SpireInsertLocator {
		public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
			// 找到 "amount" 字段的访问点，这是 VulnerablePower 的核心字段
			Matcher amountCheckMatcher = new Matcher.FieldAccessMatcher(VulnerablePower.class, "amount");
			// 返回匹配的行数，用于插入代码
			return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), amountCheckMatcher);
		}
	}
}

