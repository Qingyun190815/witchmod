package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;

import witchmod.WitchMod;

public class SkullFlaskPower extends AbstractWitchPower {
    // 能力的唯一标识符
    private static final String POWER_ID = "SkullFlaskPower";

    // 能力的图标文件路径
    public static final String IMG = "powers/skullflask.png";

    /**
     * SkullFlaskPower 构造方法
     * @param owner 此能力的持有者
     * @param amount 能力生效时提供的力量值
     */
    public SkullFlaskPower(AbstractCreature owner, int amount) {
        super(POWER_ID);  // 调用父类构造方法设置能力 ID

        // 设置能力的持有者为传入的 owner（目标）
        this.owner = owner;

        // 设置力量值
        this.amount = amount;

        // 更新能力描述
        this.updateDescription();

        // 设置该能力的图标
        this.img = new Texture(WitchMod.getResourcePath(IMG));

        // 设置该能力的类型为增益效果（Buff）
        this.type = PowerType.BUFF;
    }

    /**
     * 更新能力的描述文本
     */
    @Override
    public void updateDescription() {
        // 描述能力的效果，提供给持有者的力量值
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    /**
     * 在回合结束时触发的能力效果
     * 在回合结束时减少持有者的力量，并移除该能力。
     * @param isPlayer 是否是玩家的回合
     */
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        flash();  // 闪烁提示，表示效果生效

        // 减少持有者的力量值
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, StrengthPower.POWER_ID, amount));

        // 移除该能力
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
    }
}
