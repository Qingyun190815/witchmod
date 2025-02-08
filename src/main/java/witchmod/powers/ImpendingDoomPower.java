package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import witchmod.WitchMod;

/**
 * ImpendingDoomPower 类表示一种能力，使目标在下一回合结束时失去一定的生命值。
 * 该能力会在目标回合结束时触发一次性伤害。
 */
public class ImpendingDoomPower extends AbstractWitchPower {
    // 完整的能力 ID，用于标识此能力
    private static final String POWER_ID = "ImpendingDoom";

    // 能力图标的路径，用于显示此能力的图像
    public static final String IMG = "powers/impendingdoom.png";

    // 伤害来源对象（例如，攻击者或施加能力的角色）
    private AbstractCreature source;

    /**
     * ImpendingDoomPower 的构造方法。
     * @param owner 此能力的持有者（目标）
     * @param source 伤害来源的角色
     * @param amount 将要失去的生命值（伤害量）
     */
    public ImpendingDoomPower(AbstractCreature owner, AbstractCreature source, int amount) {
        super(POWER_ID);  // 调用父类构造方法设置能力 ID

        // 设置能力的持有者为传入的 owner（目标）
        this.owner = owner;

        // 设置伤害来源角色
        this.source = source;

        // 设置能力的强度（amount），即伤害量
        this.amount = amount;

        // 更新能力的描述
        this.updateDescription();

        // 设置能力图标
        this.img = new Texture(WitchMod.getResourcePath(IMG));

        // 设置该能力为负面效果（DEBUFF）
        this.type = PowerType.DEBUFF;

        // 该能力是基于回合的（在每回合结束时触发）
        this.isTurnBased = true;
    }

    /**
     * 更新能力的描述文本。
     */
    @Override
    public void updateDescription() {
        // 格式化描述，显示将在下回合结束时造成的伤害量
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    /**
     * 在回合结束时触发此能力效果。
     * 如果当前房间是战斗阶段且敌人未死亡，则对目标造成伤害。
     * @param isPlayer 是否是玩家的回合
     */
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 确保在战斗阶段且敌人未死亡时才触发能力
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            // 触发闪烁效果，表示能力激活
            flash();

            // 对目标造成伤害，伤害量为 ability 的 amount
            AbstractDungeon.actionManager.addToBottom(new DamageAction(owner,
                    new DamageInfo(source, amount, DamageType.HP_LOSS), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

            // 移除 ImpendingDoomPower
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, source, ID));
        }
    }
}
