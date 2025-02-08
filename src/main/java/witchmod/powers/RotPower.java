package witchmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import witchmod.WitchMod;
import witchmod.actions.RotLoseHPAction;

public class RotPower extends AbstractWitchPower implements HealthBarRenderPower {
    // 能力的唯一标识符
    private static final String POWER_ID = "Rot";
    // 完整的能力标识符
    public static final String POWER_ID_FULL = "WitchMod:Rot";

    // 能力的图标文件路径
    public static final String IMG = "powers/rot.png";

    // 源角色（通常是造成“腐化”效果的敌人）
    private AbstractCreature source;

    // 是否具有传染性
    public boolean contagious;

    /**
     * RotPower 的构造方法。
     * @param owner 此能力的持有者
     * @param source 腐化的源（通常是敌人）
     * @param amount 腐化伤害的初始值
     */
    public RotPower(AbstractCreature owner, AbstractCreature source, int amount) {
        this(owner, source, amount, false);
    }

    /**
     * RotPower 的构造方法。
     * @param owner 此能力的持有者
     * @param source 腐化的源（通常是敌人）
     * @param amount 腐化伤害的初始值
     * @param contagious 是否具有传染性
     */
    public RotPower(AbstractCreature owner, AbstractCreature source, int amount, boolean contagious) {
        super(POWER_ID);  // 调用父类构造方法设置能力 ID

        // 设置能力的持有者为传入的 owner（目标）
        this.owner = owner;

        // 设置腐化的源（通常是敌人）
        this.source = source;

        // 设置腐化伤害的强度（amount）
        this.amount = amount;

        // 更新能力的描述
        this.updateDescription();

        // 设置该能力的图标
        this.img = new Texture(WitchMod.getResourcePath(IMG));

        // 设置能力为回合制，意味着它的效果每回合都会触发
        this.isTurnBased = true;

        // 设置该能力的类型为负面效果（Debuff）
        this.type = PowerType.DEBUFF;

        // 设置是否传染
        this.contagious = contagious;

        // 如果是传染性腐化，修改能力名称
        if (this.contagious) {
            this.name = DESCRIPTIONS[3] + " " + this.name;
        }
    }

    /**
     * 更新能力的描述文本。
     * 根据腐化的强度（amount）和是否具有传染性，更新能力描述。
     */
    @Override
    public void updateDescription() {
        // 描述腐化带来的伤害
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];

        // 如果是传染性腐化，添加额外的描述
        if (contagious) {
            description += DESCRIPTIONS[2];
        }
    }

    /**
     * 在每回合开始时触发该能力的效果：
     * - 持有者会失去一定的生命值（根据腐化的强度）。
     * - 如果是传染性腐化，可能会传播到其他敌人。
     */
    @Override
    public void atStartOfTurn() {
        // 如果当前在战斗阶段，并且没有所有怪物死掉
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            // 闪烁提示，表示腐化效果生效
            flashWithoutSound();

            // 触发失去生命的动作
            AbstractDungeon.actionManager.addToBottom(new RotLoseHPAction(owner, source, amount, contagious, AbstractGameAction.AttackEffect.POISON));
        }
    }

    /**
     * 获取生命条的显示值。对于腐化效果，返回腐化强度（amount）。
     * @return 生命条的显示值
     */
    @Override
    public int getHealthBarAmount() {
        return amount;
    }

    /**
     * 获取生命条的颜色。在腐化效果中，返回暗灰色。
     * @return 生命条的颜色
     */
    @Override
    public Color getColor() {
        return Color.DARK_GRAY;
    }
}
