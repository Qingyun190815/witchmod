package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;

import witchmod.WitchMod;

/**
 * AthamePower 类表示与 Athame（魔法刀）相关的增益效果。
 * 该能力增加获得稀有卡片的几率，通常与特殊的稀有卡片掉落机制相关联。
 */
public class AthamePower extends AbstractWitchPower {
    // 完整的能力 ID，用于标识此能力
    public static final String POWER_ID_FULL = "WitchMod:AthamesOffering";

    // 简短的能力 ID，通常用于内部处理
    private static final String POWER_ID = "AthamesOffering";

    // 能力图标的路径，用于显示此能力的图像
    public static final String IMG = "powers/athame.png";

    /**
     * AthamePower 的构造方法。
     * @param owner 持有该能力的生物（通常是玩家）
     * @param amount 此能力的增强数值（增加的几率）
     */
    public AthamePower(AbstractCreature owner, int amount) {
        // 调用父类构造方法，初始化能力 ID
        super(POWER_ID);

        // 设置能力持有者
        this.owner = owner;

        // 设置能力的增强值
        this.amount = amount;

        // 更新能力描述文本
        this.updateDescription();

        // 设置能力的图标（加载图像）
        this.img = new Texture(WitchMod.getResourcePath(IMG));

        // 设置能力类型为 BUFF（增益）
        this.type = PowerType.BUFF;
    }

    /**
     * 更新能力的描述文本。
     * 根据能力的增强值（amount），更新能力的描述内容，
     * 例如，显示“增加 #bX% 的稀有卡片掉落几率”。
     */
    @Override
    public void updateDescription() {
        // 更新描述文本，`DESCRIPTIONS[0]` 和 `DESCRIPTIONS[1]` 应为类中预定义的描述字符串
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
