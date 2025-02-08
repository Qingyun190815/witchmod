package witchmod.cards;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import witchmod.actions.CleanseAction;

public abstract class AbstractWitchCleansableCurse extends AbstractWitchCard {
    public boolean cleansed = false;  // 是否已经净化
    public boolean checkAtTurnStart = true;  // 是否在回合开始时检查
    public boolean checkAtTurnEnd = true;  // 是否在回合结束时检查
    public boolean checkDuringTurn = true;  // 是否在回合进行中检查

    // 构造方法，创建一个可净化的诅咒卡片
    public AbstractWitchCleansableCurse(String id, String img, CardRarity rarity) {
        super(id, img, -2, CardType.CURSE, rarity, CardTarget.NONE);
    }

    // 具体的净化检查逻辑
    protected boolean cleanseCheck() {
        return false;  // 默认情况下不会进行清除操作，具体逻辑由子类决定
    }

    @Override
    public void atTurnStart() {
        if (checkAtTurnStart && AbstractDungeon.player.hand.contains(this)) {
            doCleanseCheck();  // 如果卡片在手牌中，且符合条件，则执行清除检查
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (checkDuringTurn && AbstractDungeon.player.hand.contains(this)) {
            doCleanseCheck();  // 每回合进行中检查清除
        }
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if (checkAtTurnEnd && AbstractDungeon.player.hand.contains(this)) {
            doCleanseCheck();  // 每回合结束时进行清除检查
        }
    }

    // 执行清除检查的逻辑
    private void doCleanseCheck() {
        if (cleansed == false && cleanseCheck()) {  // 如果卡片未清除且满足清除条件
            AbstractDungeon.actionManager.addToTop(new CleanseAction(this));  // 添加清除行动
        }
    }

    // 执行净化，并且是否应用清除后的效果
    public void cleanse(boolean applyPowers) {
        cardPreviewTooltip = null;  // 清除卡片预览的提示
        cleansed = true;  // 标记卡片已净化
        if (applyPowers) {
            applyPowers();  // 如果需要应用清除后的效果，则调用 applyPowers
        }
    }

    // 默认执行净化并应用清除效果
    public void cleanse() {
        cleanse(true);
    }
}
