package witchmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class RiteOfSummerDamageAction extends AbstractGameAction {
    private DamageInfo info;  // 储存伤害信息

    // 构造函数：初始化目标、伤害信息和攻击次数
    public RiteOfSummerDamageAction(AbstractCreature target, DamageInfo info, int amount) {
        this.target = target;  // 设置目标
        this.info = info;  // 设置伤害信息
        this.amount = amount;  // 设置攻击次数
        this.duration = Settings.ACTION_DUR_FAST;  // 设置为快速执行
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;  // 动作类型为卡牌操作
        this.attackEffect = AttackEffect.FIRE;  // 攻击效果为火焰
    }

    @Override
    public void update() {
        if (target == null) {  // 如果目标为空，结束动作
            isDone = true;
            return;
        }
        // 如果当前房间的怪物都已经死了，清空后续的战斗动作并结束
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            isDone = true;
            return;
        }
        // 如果目标的生命值大于 0，则执行攻击
        if (target.currentHealth > 0) {
            // 显示攻击效果
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
            // 应用伤害效果（例如伤害增益或减益）
            info.applyPowers(info.owner, target);
            // 对目标造成伤害
            target.damage(info);
            // 如果攻击次数大于 1，则递归调用此动作对其他目标造成伤害
            if (amount > 1 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                amount--;  // 减少攻击次数
                // 添加新的攻击动作到战斗队列
                AbstractDungeon.actionManager.addToTop(new RiteOfSummerDamageAction(AbstractDungeon.getMonsters().getRandomMonster(true), info, amount));
            }
            // 添加一个等待动作，暂停 0.1 秒
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.1f));
        }
        isDone = true;  // 标记动作完成
    }
}
