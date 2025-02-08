package witchmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class LoseDexterityPower extends AbstractWitchPower {
    // 能力的唯一标识符
    public static final String POWER_ID = "Twitch";

    // 能力的名称
    public static final String NAME = "Twitch";

    // 能力描述文本数组：用于描述该能力的效果
    public static final String[] DESCRIPTIONS = new String[] {
            "临时获得 #b",
            " 点 #y敏捷."
    };

    /**
     * LoseDexterityPower 的构造方法。
     * @param owner 此能力的持有者
     * @param newAmount 能力的强度，表示增加的敏捷值
     */
    public LoseDexterityPower(AbstractCreature owner, int newAmount) {
        super(POWER_ID);  // 调用父类构造方法设置能力 ID

        // 设置能力的持有者为传入的 owner（目标）
        this.owner = owner;

        // 设置能力的强度（newAmount），表示增加的敏捷值
        this.amount = newAmount;

        // 更新能力的描述
        this.updateDescription();

        // 设置该能力的渲染颜色（Chartreuse 色）
        this.renderColor = Color.CHARTREUSE;

        // 加载该能力的图标区域
        this.loadRegion("flex");
    }

    /**
     * 更新能力的描述文本。
     * 根据能力的强度（amount），更新描述的文本。
     */
    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    /**
     * 在回合结束时触发该能力的效果：
     * - 从持有者的 DexterityPower 中减少相应的敏捷值。
     * - 移除该能力。
     */
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 闪光提示，表示该能力效果即将生效
        flash();

        // 从持有者的 DexterityPower 中减少该能力增加的敏捷值
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, DexterityPower.POWER_ID, amount));

        // 移除该能力
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
    }
}
