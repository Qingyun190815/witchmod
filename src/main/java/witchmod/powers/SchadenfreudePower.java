package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import com.megacrit.cardcrawl.powers.ArtifactPower;
import witchmod.WitchMod;

public class SchadenfreudePower extends AbstractWitchPower {
    // 能力的唯一标识符
    private static final String POWER_ID = "SchadenfreudePower";

    // 能力的图标文件路径
    public static final String IMG = "powers/schadenfreude.png";

    /**
     * SchadenfreudePower 构造方法
     * @param owner 此能力的持有者
     * @param amount 能力生效时提供的护盾量
     */
    public SchadenfreudePower(AbstractCreature owner, int amount) {
        super(POWER_ID);  // 调用父类构造方法设置能力 ID

        // 设置能力的持有者为传入的 owner（目标）
        this.owner = owner;

        // 设置护盾量
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
        // 描述能力的效果，提供给持有者护盾的数量
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    /**
     * 当一个能力（Debuff）被施加到敌人身上时触发该方法
     * 如果条件满足（施加的是负面效果，来源是持有者，目标不是持有者且目标没有 ArtifactPower），
     * 则为持有者提供护盾。
     * @param power 被施加的能力
     * @param target 目标角色
     * @param source 能力的施加源
     */
    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        // 检查该能力是否为负面效果（Debuff），并且施加的源是持有者
        if (power.type == AbstractPower.PowerType.DEBUFF && !power.ID.equals("Shackled") && source == owner && target != owner && !target.hasPower(ArtifactPower.POWER_ID)) {
            flash();  // 闪烁提示，表示效果生效
            // 为持有者提供护盾
            AbstractDungeon.actionManager.addToTop(new GainBlockAction(owner, target, amount));
        }
    }
}
