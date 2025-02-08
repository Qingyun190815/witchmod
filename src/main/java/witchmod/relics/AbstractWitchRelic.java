package witchmod.relics;  // 归属于witchmod.relics包

import com.badlogic.gdx.graphics.Texture;  // 导入用于加载纹理的类

import basemod.abstracts.CustomRelic;  // 引入BaseMod提供的自定义遗物基类
import witchmod.WitchMod;  // 引入主MOD类

/**
 * 抽象类 AbstractWitchRelic（巫师遗物基类）
 * 该类继承自 CustomRelic（自定义遗物），
 * 主要用于简化巫师MOD遗物的创建过程。
 */
public abstract class AbstractWitchRelic extends CustomRelic {

	/**
	 * 构造方法
	 *
	 * @param id 遗物的唯一标识符
	 * @param img 遗物的图片资源路径（相对于MOD资源路径）
	 * @param tier 遗物的稀有度（RelicTier.COMMON/UNCOMMON/RARE/BOSS/SHOP）
	 * @param sfx 遗物拾取时的音效（LandingSound枚举）
	 */
	public AbstractWitchRelic(String id, String img, RelicTier tier, LandingSound sfx) {
		// 调用父类CustomRelic的构造方法
		super(id, new Texture(WitchMod.getResourcePath(img)), tier, sfx);
	}
}
