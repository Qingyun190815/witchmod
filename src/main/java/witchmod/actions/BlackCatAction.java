package witchmod.actions;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.SingingBowlButton;
import com.megacrit.cardcrawl.ui.buttons.SkipCardButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;

import basemod.ReflectionHacks;

public class BlackCatAction extends AbstractGameAction {
    private boolean retrieveCard = false;

    // 构造方法：设置动作类型
    public BlackCatAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST; // 设置持续时间（短动画）
    }

    @Override
    public void update() {
        // 初次调用时
        if (duration == Settings.ACTION_DUR_FAST) {
            // 重置卡牌奖励屏幕
            AbstractDungeon.cardRewardScreen.reset();

            // 清理屏幕上的奖励卡牌
            AbstractDungeon.cardRewardScreen.rItem = null;
            AbstractDungeon.cardRewardScreen.discoveryCard = null;
            AbstractDungeon.cardRewardScreen.codexCard = null;

            // 通过反射修改 CardRewardScreen 的 discovery 变量（标记为探索卡牌选择）
            ReflectionHacks.setPrivate(AbstractDungeon.cardRewardScreen, CardRewardScreen.class, "discovery", true);

            // 隐藏跳过卡牌和圣碗按钮
            ((SingingBowlButton) ReflectionHacks.getPrivate(AbstractDungeon.cardRewardScreen, CardRewardScreen.class, "bowlButton")).hide();
            ((SkipCardButton) ReflectionHacks.getPrivate(AbstractDungeon.cardRewardScreen, CardRewardScreen.class, "skipButton")).hide();

            // 取消悬停 UI
            AbstractDungeon.topPanel.unhoverHitboxes();

            // 生成 3 张不同的诅咒牌
            ArrayList<AbstractCard> curseChoices = new ArrayList<>();
            while (curseChoices.size() < 3) {
                AbstractCard tmp = AbstractDungeon.returnRandomCurse();
                boolean isDuplicate = false;

                // 避免重复卡牌
                for (AbstractCard c : curseChoices) {
                    if (c.cardID.equals(tmp.cardID)) {
                        isDuplicate = true;
                        break;
                    }
                }
                if (!isDuplicate) {
                    curseChoices.add(tmp.makeCopy());
                }
            }

            // 设置奖励屏幕上的卡牌
            AbstractDungeon.cardRewardScreen.rewardGroup = curseChoices;

            // 显示卡牌选择 UI
            AbstractDungeon.isScreenUp = true;
            AbstractDungeon.screen = AbstractDungeon.CurrentScreen.CARD_REWARD;
            AbstractDungeon.dynamicBanner.appear("将一张诅咒牌加入你的抽牌堆"); // UI 文本
            AbstractDungeon.overlayMenu.showBlackScreen();

            // 调整卡牌显示位置
            placeCards((float) Settings.WIDTH / 2.0f, Settings.HEIGHT * 0.45f);

            // 标记卡牌已被见过
            for (AbstractCard c : curseChoices) {
                UnlockTracker.markCardAsSeen(c.cardID);
            }

            // 结束第一阶段
            tickDuration();
            return;
        }

        // 选择卡牌后触发
        if (!retrieveCard) {
            if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
                AbstractCard curse = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();

                // 如果诅咒牌是固有（Innate）的，则直接加入抽牌堆顶部，否则加入抽牌堆底部
                AbstractDungeon.effectList.add(0, new ShowCardAndAddToDrawPileEffect(
                        curse, Settings.WIDTH / 2, Settings.HEIGHT / 2, !curse.isInnate));

                // 清除选择
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
            }
            retrieveCard = true;
        }

        // 结束动作
        tickDuration();
    }

    /**
     *  设置奖励屏幕上卡牌的位置
     */
    private void placeCards(float x, float y) {
        AbstractDungeon.cardRewardScreen.rewardGroup.get(0).target_x = x - AbstractCard.IMG_WIDTH - 40.0f * Settings.scale;
        AbstractDungeon.cardRewardScreen.rewardGroup.get(1).target_x = x;
        AbstractDungeon.cardRewardScreen.rewardGroup.get(2).target_x = x + AbstractCard.IMG_WIDTH + 40.0f * Settings.scale;

        AbstractDungeon.cardRewardScreen.rewardGroup.get(0).target_y = y;
        AbstractDungeon.cardRewardScreen.rewardGroup.get(1).target_y = y;
        AbstractDungeon.cardRewardScreen.rewardGroup.get(2).target_y = y;

        // 设置缩放比例
        for (AbstractCard c : AbstractDungeon.cardRewardScreen.rewardGroup) {
            c.drawScale = 0.75f;
            c.targetDrawScale = 0.75f;
            c.current_x = x;
            c.current_y = y;
        }
    }
}
