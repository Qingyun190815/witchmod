package witchmod.relics;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import witchmod.actions.BlackCatAction;

public class BlackCat extends AbstractWitchRelic {
	// 遗物 ID（唯一标识符）
	public static final String ID = "BlackCat";

	// 遗物属性
	private static final RelicTier TIER = RelicTier.STARTER; // 遗物等级（起始遗物）
	private static final String IMG = "relics/blackcat.png"; // 遗物图片路径
	private static final LandingSound SOUND = LandingSound.FLAT; // 触发音效

	// 构造方法
	public BlackCat() {
		super(ID, IMG, TIER, SOUND);
	}

	// 获取遗物描述文本
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0]; // 读取描述，支持多语言
	}

	// 战斗开始时触发
	@Override
	public void atBattleStartPreDraw() {
		// 触发 BlackCatAction（随机选择 1 个诅咒并洗入抽牌堆）
		AbstractDungeon.actionManager.addToBottom(new BlackCatAction());

		// 显示遗物特效（遗物浮现在角色上方）
		AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));

		// 遗物 UI 闪光，提示玩家效果生效
		flash();
	}

	// 每次抽牌时触发
	@Override
	public void onCardDraw(AbstractCard drawnCard) {
		// 如果抽到的是诅咒牌（Curse）
		if (drawnCard.type == CardType.CURSE) {
			// 增加 1 点能量 [B]
			AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
			// 遗物 UI 闪光
			flash();
		}
	}

	// 复制遗物（用于某些复制机制，如 "Bottled Relic"）
	@Override
	public AbstractRelic makeCopy() {
		return new BlackCat();
	}
}
