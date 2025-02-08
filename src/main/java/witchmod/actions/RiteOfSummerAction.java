package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RiteOfSummerAction extends AbstractGameAction {
    private AbstractPlayer player;  // 当前玩家
    public static final String TEXT = "弃置.";  // 动作提示文本
    private DamageInfo info;  // 造成的伤害信息

    // 构造函数：初始化动作
    public RiteOfSummerAction(DamageInfo info) {
        this.info = info;  // 设置伤害信息
        this.duration = Settings.ACTION_DUR_FAST;  // 设置为快速动作
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;  // 动作类型为卡牌操作
        this.player = AbstractDungeon.player;  // 获取当前玩家对象
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {  // 快速执行
            if (player.hand.size() == 0) {  // 如果玩家手牌为空，结束动作
                isDone = true;
                return;
            }
            // 打开卡牌选择界面，让玩家选择要丢弃的卡牌
            AbstractDungeon.handCardSelectScreen.open(TEXT, player.hand.size(), true, true);
            tickDuration();  // 更新动作持续时间
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {  // 如果卡牌已被选择
            if (AbstractDungeon.handCardSelectScreen.selectedCards.group.size() > 0) {
                int count = 0;
                // 将玩家选择的卡牌移到弃牌堆
                for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                    player.hand.moveToDiscardPile(c);
                    count++;  // 记录丢弃的卡牌数量
                }
                // 使用选中的卡牌数量和伤害信息创建一个新的行动，造成伤害
                AbstractDungeon.actionManager.addToBottom(new RiteOfSummerDamageAction(AbstractDungeon.getMonsters().getRandomMonster(true), info, count));
            }
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;  // 标记卡牌已取回
        }
        tickDuration();  // 更新动作持续时间
    }
}
