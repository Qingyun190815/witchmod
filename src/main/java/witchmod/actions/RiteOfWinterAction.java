package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RiteOfWinterAction extends AbstractGameAction {
    private AbstractPlayer player;  // 玩家实例
    public static final String TEXT = "置于抽牌堆顶.";  // 选择卡片的文本提示

    // 构造方法：接受一个数量，用于设置该动作的效果
    public RiteOfWinterAction(int amount) {
        this.amount = amount;  // 设置增加的格挡值
        this.duration = Settings.ACTION_DUR_FAST;  // 设置为快速执行
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;  // 动作类型为卡牌操作
        this.player = AbstractDungeon.player;  // 获取玩家实例
    }

    @Override
    public void update() {
        // 如果当前是快速执行阶段
        if (duration == Settings.ACTION_DUR_FAST) {
            // 如果手牌为空，直接结束动作
            if (player.hand.size() == 0) {
                isDone = true;
                return;
            }
            // 打开手牌选择屏幕，允许玩家选择要放回牌堆的卡片
            AbstractDungeon.handCardSelectScreen.open(TEXT, player.hand.size(), true, true);
            tickDuration();  // 刷新动作的持续时间
            return;
        }

        // 如果手牌已被选择，处理选中的卡片
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            if (AbstractDungeon.handCardSelectScreen.selectedCards.group.size() > 0) {
                int count = 0;
                // 将选中的卡片移动到牌堆
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    player.hand.moveToDeck(c, false);
                    count++;  // 记录选中的卡片数量
                }
                // 在战斗管理队列中加入一个增加格挡的动作，数量等于选中卡片数乘以增加的格挡值
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player, player, amount * count));
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;  // 标记卡片已被选择
        }
        tickDuration();  // 刷新动作的持续时间
    }
}
