package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;

import witchmod.WitchMod;

/**
 * DemonfyrePower 类表示一种增益能力，用于增加 Demonfyre 卡牌的伤害。
 */
public class DemonfyrePower extends AbstractWitchPower {
    // 完整的能力 ID，用于标识此能力
    public static final String POWER_ID_FULL = "WitchMod:DemonfyrePower";

    // 能力 ID，用于标识该能力
    private static final String POWER_ID = "DemonfyrePower";

    // 能力图标的路径，用于显示此能力的图像
    public static final String IMG = "powers/demonfyre.png";

    /**
     * DemonfyrePower 的构造方法。
     * @param owner 此能力的持有者（通常为玩家）
     * @param amount 此能力的强度，通常影响伤害加成的数值
     */
    public DemonfyrePower(AbstractCreature owner, int amount) {
        super(POWER_ID);  // 调用父类构造方法设置能力 ID

        // 设置能力的持有者为传入的 owner（玩家或其他角色）
        this.owner = owner;

        // 设置能力的强度（amount）
        this.amount = amount;

        // 更新能力的描述
        this.updateDescription();

        // 设置能力图标
        this.img = new Texture(WitchMod.getResourcePath(IMG));

        // 设置该能力为增益（BUFF）
        this.type = PowerType.BUFF;
    }

    /**
     * 更新能力的描述文本。
     * @return 能力的描述
     */
    @Override
    public void updateDescription() {
        // 格式化描述，显示增加的伤害数值
        description = DESCRIPTIONS[0] + amount;
    }
}
