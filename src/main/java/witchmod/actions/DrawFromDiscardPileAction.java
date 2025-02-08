package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DrawFromDiscardPileAction extends AbstractGameAction {
    public static final String TEXT = "选择一张牌加入手牌";
    private AbstractPlayer p = AbstractDungeon.player;
    private Boolean ignoreCost = false;  // 是否忽略卡片费用
    private Boolean random = false;     // 是否从弃牌堆随机抽取卡片

    // 构造方法，初始化动作参数
    public DrawFromDiscardPileAction(int cards, Boolean random, Boolean ignoreCost) {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FASTER;  // 设置动作持续时间
        this.random = random;   // 是否随机抽取卡片
        this.ignoreCost = ignoreCost;  // 是否忽略费用
        this.amount = cards;  // 需要抽取的卡片数量
    }

    @Override
    public void update() {
        // 如果当前房间正在结束战斗，则不执行任何操作
        if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
            isDone = true;
            return;
        }

        // 如果持续时间为快速（一次性动作），执行抽卡逻辑
        if (duration == Settings.ACTION_DUR_FASTER) {
            // 如果弃牌堆为空，动作结束
            if (p.discardPile.isEmpty()) {
                isDone = true;
                return;
            }

            // 如果弃牌堆中只有一张卡片，则直接抽取它
            if (p.discardPile.size() == 1) {
                AbstractCard tmp = p.discardPile.getTopCard();
                drawCard(tmp);
            } else if (!random) {
                // 如果不随机抽卡，打开弃牌堆选择界面
                if (p.discardPile.group.isEmpty()) {
                    isDone = true;
                } else {
                    AbstractDungeon.gridSelectScreen.open(p.discardPile, 1, TEXT, false, false, true, false);
                }
            } else {
                // 如果随机抽卡，则从弃牌堆中随机抽取卡片
                for (int i = 0; i < Math.min(amount, p.discardPile.size()); i++) {
                    AbstractCard card = p.discardPile.getRandomCard(true);
                    drawCard(card);
                }
            }

            // 执行动作并返回
            this.tickDuration();
            return;
        }

        // 如果选择了卡片，从弃牌堆抽取它们
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                drawCard(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            isDone = true;
        }

        // 更新弃牌堆布局
        p.discardPile.refreshHandLayout();
        this.tickDuration();
    }

    // 抽取一张卡片并加入到玩家手牌
    private void drawCard(AbstractCard card) {
        // 如果忽略费用，设置卡片为免费
        if (ignoreCost) {
            card.freeToPlayOnce = true;
        }

        // 计算卡片的加成效果
        card.applyPowers();

        // 将卡片从弃牌堆移到牌堆
        AbstractDungeon.player.discardPile.moveToDeck(card, false);

        // 添加卡片到抽卡动作
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
    }
}
