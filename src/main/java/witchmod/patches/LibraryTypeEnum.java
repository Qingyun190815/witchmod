package witchmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;  // 引入SpireEnum，允许定义新的枚举
import com.megacrit.cardcrawl.helpers.CardLibrary;  // 引入CardLibrary类，用于卡片库管理

public class LibraryTypeEnum {

	@SpireEnum  // 该注解允许在CardLibrary.LibraryType中添加新的枚举值
	public static CardLibrary.LibraryType WITCH;  // 定义新的枚举值WITCH，表示巫师卡片类型

}
