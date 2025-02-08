package witchmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import witchmod.powers.RotPower;

public class RotLoseHPAction extends AbstractGameAction {
    private static final float DURATION = 0.33f;  // 动作持续时间
    private boolean contagious;  // 是否传播腐化效果

    // 构造方法：目标、来源、伤害值、是否传播腐化、攻击效果
    public RotLoseHPAction(AbstractCreature target, AbstractCreature source, int amount, boolean contagious, AbstractGameAction.AttackEffect effect) {
        this.setValues(target, source, amount);  // 设置目标、来源和伤害值
        this.actionType = AbstractGameAction.ActionType.DAMAGE;  // 动作类型为伤害
        this.attackEffect = effect;  // 设置攻击效果
        this.duration = DURATION;  // 设置动作的持续时间
        this.contagious = contagious;  // 设置是否传播腐化效果
    }

    @Override
    public void update() {
        // 如果当前不是战斗阶段，结束动作
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            isDone = true;
            return;
        }

        // 如果是第一次执行且目标仍然存活，播放攻击效果
        if (duration == DURATION && target.currentHealth > 0) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
        }

        this.tickDuration();  // 更新动作持续时间

        if (this.isDone) {
            // 获取目标的腐化效果
            AbstractPower rotPower = target.getPower(RotPower.POWER_ID_FULL);
            if (rotPower != null) {
                int potency = rotPower.amount;  // 获取腐化效果的强度

                // 如果是传染性腐化且目标当前生命值小于或等于伤害值，传播腐化到一个随机怪物
                if (contagious && !this.target.isPlayer && target.currentHealth <= this.amount) {
                    AbstractMonster newTarget = AbstractDungeon.getRandomMonster((AbstractMonster) target);
                    if (newTarget != null) {
                        // 如果目标已经有腐化效果，增强腐化效果
                        if (newTarget.hasPower(RotPower.POWER_ID_FULL)) {
                            newTarget.getPower(RotPower.POWER_ID_FULL).amount += potency;
                            ((RotPower)newTarget.getPower(RotPower.POWER_ID_FULL)).contagious = true;  // 设置为传播腐化
                        } else {
                            // 否则为目标添加腐化效果
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(newTarget, source, new RotPower(newTarget, source, potency, true), potency));
                        }
                    }
                }

                // 如果目标仍然存活，造成伤害并增加腐化效果的强度
                if (target.currentHealth > 0) {
                    target.tint.color = Color.BROWN.cpy();  // 设置目标的颜色为棕色（腐化的视觉效果）
                    target.tint.changeColor(Color.WHITE.cpy());  // 恢复为原始颜色
                    target.damage(new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS));  // 对目标造成伤害
                    rotPower.amount += 1;  // 增加腐化效果强度
                    rotPower.updateDescription();  // 更新腐化效果描述
                }
            }

            // 如果所有怪物都已死亡，清除后续战斗动作
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            // 在完成操作后，加入一个短暂的等待动作
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.1f));
        }
    }
}
