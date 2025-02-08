package witchmod.relics;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction; // 导入制作临时卡片的操作
import com.megacrit.cardcrawl.dungeons.AbstractDungeon; // 导入游戏中的地下城类
import com.megacrit.cardcrawl.relics.AbstractRelic; // 导入遗物的基础类

import witchmod.powers.SummonFamiliarPower; // 导入召唤熟悉物的力量类

public class BirdCage extends AbstractWitchRelic { // BirdCage类继承自自定义的AbstractWitchRelic类
	public static final String ID = "PetCage"; // 设置遗物的唯一ID
	private static final RelicTier TIER = RelicTier.RARE; // 设置遗物的稀有度为RARE
	private static final String IMG = "relics/birdcage.png"; // 设置遗物的图像路径
	private static final LandingSound SOUND = LandingSound.CLINK; // 设置遗物掉落时的声音效果

	public BirdCage() {
		super(ID, IMG, TIER, SOUND); // 调用父类构造函数，传入遗物的ID、图像路径、稀有度和声音效果
	}

	@Override
	public String getUpdatedDescription() { // 获取遗物的描述文本
		return DESCRIPTIONS[0]; // 返回第一个描述
	}

	@Override
	public void atTurnStart() { // 在每个回合开始时调用
		// 使用MakeTempCardInHandAction创建一个临时卡片，并加入到玩家的手牌中
		AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SummonFamiliarPower.getRandomFamiliarCard()));
		flash(); // 触发遗物的闪烁效果
	}

	@Override
	public AbstractRelic makeCopy() { // 创建该遗物的副本
		return new BirdCage(); // 返回新的BirdCage对象
	}
}
