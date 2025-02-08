package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RiteOfAutumnAction extends AbstractGameAction {
    private AbstractPlayer player;  // 记录当前玩家
    public static final String TEXT = "消耗.";  // 文字提示，表示要“消耗”选中的卡牌

    // 构造函数：初始化动作
    public RiteOfAutumnAction() {
        this.duration = Settings.ACTION_DUR_FAST;  // 设置为快速动作
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;  // 动作类型为卡牌操作
        this.player = AbstractDungeon.player;  // 获取当前玩家对象
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {  // 快速执行
            if (player.hand.size() == 0) {  // 如果玩家手牌为空，直接结束
                isDone = true;
                return;
            }
            // 打开手牌选择界面，让玩家选择要消耗的卡牌
            AbstractDungeon.handCardSelectScreen.open(TEXT, player.hand.size(), true, true);
            tickDuration();  // 更新动作持续时间
            return;
        }

        // 如果玩家已选择卡牌
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            if (AbstractDungeon.handCardSelectScreen.selectedCards.group.size() > 0) {
                // 将选中的卡牌从手牌移动到“消耗”堆
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    player.hand.moveToExhaustPile(c);
                }
                // 选中卡牌的数量等于消耗卡牌后，玩家会从卡堆中抽卡
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(player, AbstractDungeon.handCardSelectScreen.selectedCards.group.size()));
            }
            // 标记为已取回卡牌
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        tickDuration();  // 更新动作持续时间
    }
}
