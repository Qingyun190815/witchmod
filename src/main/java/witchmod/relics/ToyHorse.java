package witchmod.relics;

import java.util.List;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class ToyHorse extends AbstractWitchRelic {
	public static final String ID = "ToyHorse";  // 遗物 ID
	private static final RelicTier TIER = RelicTier.COMMON;  // 遗物等级（COMMON: 普通）
	private static final String IMG = "relics/toyhorse.png";  // 遗物图片路径
	private static final LandingSound SOUND = LandingSound.CLINK;  // 遗物声音效果
	public boolean cardSelected = false;  // 是否已经选择了卡牌

	// 构造函数：初始化遗物
	public ToyHorse() {
		super(ID, IMG, TIER, SOUND);
	}

	// 获取遗物的描述
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];  // 返回描述文本（需要翻译）
	}

	// 装备时触发：选择卡牌加入卡组
	@Override
	public void onEquip() {
		CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);  // 创建一个卡牌组
		List<AbstractCard> list = CardLibrary.getAllCards();  // 获取所有卡牌
		for (AbstractCard c : list) {
			// 如果是基础卡牌，且不是“防御”或“打击”卡牌，则加入到卡牌组中
			if (c.rarity == CardRarity.BASIC && (!c.cardID.contains("Defend") && !c.cardID.contains("Strike"))) {
				group.addToBottom(c);  // 加入卡组
				UnlockTracker.markCardAsSeen(c.cardID);  // 标记卡牌为已见
			}
		}
		// 打开卡牌选择界面，让玩家选择一张卡牌
		AbstractDungeon.gridSelectScreen.open(group, 1, "Select a card to add to your deck", false);
	}

	// 更新方法：处理卡牌选择
	@Override
	public void update() {
		super.update();
		if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
			cardSelected = true;  // 标记为卡牌已选择
			// 显示选择的卡牌并将其加入卡组
			AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(
					AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy(),
					(float) Settings.WIDTH / 2.0f, (float) Settings.HEIGHT / 2.0f
			));
			AbstractDungeon.gridSelectScreen.selectedCards.clear();  // 清空选择的卡牌
		}
	}

	// 创建遗物的副本
	@Override
	public AbstractRelic makeCopy() {
		return new ToyHorse();  // 返回一个新的 ToyHorse 遗物副本
	}
}
