package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HandToTopOfDeckAction extends AbstractGameAction {
    public static final String TEXT = "选择要放置在抽牌堆顶部的卡片提示文本";  // 选择要放置在抽牌堆顶部的卡片提示文本
    private AbstractPlayer p = AbstractDungeon.player;  // 当前玩家对象

    // 构造方法，初始化动作
    public HandToTopOfDeckAction(AbstractCreature source, int amount) {
        this.setValues(null, source, amount);  // 设置目标和源
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;  // 设置为卡片操作类型
        this.duration = Settings.ACTION_DUR_FASTER;  // 设置持续时间为快速
    }

    @Override
    public void update() {
        // 如果当前回合已结束，则结束该动作
        if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
            isDone = true;
            return;
        }
        if (this.duration == Settings.ACTION_DUR_FASTER) {
            // 如果手牌为空，则结束该动作
            if (p.discardPile.isEmpty()) {
                isDone = true;
                return;
            }
            // 如果手牌只有一张卡片，直接将其移动到抽牌堆顶部
            if (p.hand.size() == 1) {
                AbstractCard tmp = p.hand.getTopCard();
                p.hand.removeCard(tmp);
                p.hand.moveToDeck(tmp, false);
            }
            // 如果手牌中的卡片数量大于 `amount`，打开卡片选择界面
            if (p.hand.group.size() > amount) {
                AbstractDungeon.gridSelectScreen.open(p.hand, amount, TEXT, false, false, false, false);
                tickDuration();  // 增加一段时间来等待玩家选择
                return;
            }
        }

        // 如果玩家选择了卡片，将卡片从手牌移至抽牌堆顶部
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                p.hand.removeCard(c);  // 从手牌移除卡片
                p.hand.moveToDeck(c, false);  // 将卡片移到抽牌堆顶部
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();  // 清空选择的卡片列表
            AbstractDungeon.player.hand.refreshHandLayout();  // 刷新手牌布局
        }
        tickDuration();  // 执行持续时间的递减
    }
}
