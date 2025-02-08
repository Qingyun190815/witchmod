package witchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;  // 引入SpirePatch库，用于修补游戏中的方法
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;  // 引入CharacterOption类，代表角色选择界面
import javassist.CannotCompileException;  // 引入javassist库的异常类
import javassist.expr.ExprEditor;  // 引入Expression编辑器类，用于编辑字节码
import javassist.expr.MethodCall;  // 引入MethodCall类，用于表示方法调用

@SpirePatch(
        clz = CharacterOption.class,  // 目标类为CharacterOption
        method = "renderRelics"  // 目标方法为renderRelics，显示角色遗物时调用
)
public class CharSelectionScreenNewLineInRelicDescriptionPatch {

    /**
     * Instrument方法用于修改字节码，特别是调整renderSmartText方法的行为
     */
    public static ExprEditor Instrument() {
        return new ExprEditor() {

            /**
             * edit方法用于检测并修改指定的MethodCall（方法调用）。
             * 如果方法名称是"renderSmartText"，则修改它的第七个参数（行间距）
             */
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getMethodName().equals("renderSmartText")) {
                    float lineSpacing = 30.0f;  // 设置行间距为30.0f
                    m.replace("{ $7 = " + lineSpacing + "; $_ = $proceed($$); }");  // 替换方法调用，改变行间距
                }
            }
        };
    }
}
