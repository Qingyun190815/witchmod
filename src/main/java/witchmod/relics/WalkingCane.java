package witchmod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;

public class WalkingCane extends AbstractWitchRelic {
	public static final String ID = "WalkingCane";  // 遗物 ID
	private static final RelicTier TIER = RelicTier.RARE;  // 遗物等级（RARE: 稀有）
	private static final String IMG = "relics/walkingcane.png";  // 遗物图片路径
	private static final LandingSound SOUND = LandingSound.SOLID;  // 遗物声音效果

	// 构造函数：初始化遗物
	public WalkingCane() {
		super(ID, IMG, TIER, SOUND);
	}

	// 获取遗物的描述
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];  // 返回描述文本（需要翻译）
	}

	// 创建遗物的副本
	@Override
	public AbstractRelic makeCopy() {
		return new WalkingCane();  // 返回一个新的 WalkingCane 遗物副本
	}
}
