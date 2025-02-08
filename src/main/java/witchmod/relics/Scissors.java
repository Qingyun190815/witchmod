package witchmod.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Scissors extends AbstractWitchRelic {
	public static final String ID = "Scissors";  // 遗物 ID
	private static final RelicTier TIER = RelicTier.SHOP;  // 遗物的等级（SHOP: 商店等级）
	private static final String IMG = "relics/scissors.png";  // 遗物图片
	private static final LandingSound SOUND = LandingSound.CLINK;  // 遗物的声音效果

	// 构造函数：初始化遗物
	public Scissors() {
		super(ID, IMG, TIER, SOUND);
	}

	// 获取遗物描述
	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];  // 返回第一条描述（需要翻译）
	}

	// 玩家获得卡牌时触发
	@Override
	public void onObtainCard(AbstractCard c) {
		// 如果获得的是稀有卡牌
		if (c.rarity == CardRarity.RARE) {
			AbstractCard toRemove = null;

			// 遍历玩家牌组中的每一张基础卡牌
			for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
				// 如果是基础卡牌且不是“打击”或“防御”卡牌
				if (card.rarity == CardRarity.BASIC && (!c.cardID.contains("Defend") && !c.cardID.contains("Strike"))) {
					toRemove = card;  // 选择移除这张卡牌
					break;
				}
			}

			// 如果找到了要移除的卡牌
			if (toRemove != null) {
				flash();  // 播放遗物闪烁效果
				CardCrawlGame.sound.play("CARD_BURN");  // 播放燃烧卡牌的音效
				AbstractDungeon.player.masterDeck.removeCard(toRemove);  // 从牌组中移除卡牌
			}
		}
	}

	// 创建遗物的副本
	@Override
	public AbstractRelic makeCopy() {
		return new Scissors();  // 返回一个新的 Scissors 遗物副本
	}
}
