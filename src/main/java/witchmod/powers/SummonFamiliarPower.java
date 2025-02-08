package witchmod.powers;

import com.badlogic.gdx.graphics.Texture; // 导入GDX的纹理类，用于加载图像
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction; // 导入创建临时卡片的动作类
import com.megacrit.cardcrawl.cards.AbstractCard; // 导入卡片的基础类
import com.megacrit.cardcrawl.core.AbstractCreature; // 导入角色基类
import com.megacrit.cardcrawl.dungeons.AbstractDungeon; // 导入地下城类
import witchmod.WitchMod; // 导入WitchMod类
import witchmod.cards.familiar.*; // 导入各种随从卡片类

public class SummonFamiliarPower extends AbstractWitchPower { // SummonFamiliarPower类继承自自定义的AbstractWitchPower
    private static final String POWER_ID = "SummonFamiliar"; // 力量的ID
    public static final String POWER_ID_FULL = "WitchMod:SummonFamiliar"; // 完整ID，包含mod的标识符
    //public static final String NAME = "Summon Familiar"; // 力量的名称（未使用）
    //public static final String[] DESCRIPTIONS = new String[]{ "At the start of your turn, add ", "  to your hand" }; // 描述（未使用）
    public static final String IMG = "powers/summonfamiliar.png"; // 力量图标的路径
    private FamiliarCardEnum card; // 储存所选择的随从卡片类型
    private boolean upgraded; // 储存是否升级的状态

    public SummonFamiliarPower(AbstractCreature owner, FamiliarCardEnum card, boolean upgraded) {
        super(POWER_ID); // 调用父类构造函数，传入力量的ID
        this.owner = owner; // 设置力量的拥有者
        this.upgraded = upgraded; // 设置升级状态
        this.card = card; // 设置随从卡片类型
        this.updateDescription(); // 更新力量的描述
        this.img = new Texture(WitchMod.getResourcePath(IMG)); // 设置图标
        this.type = PowerType.BUFF; // 设置为增益类型力量
    }
    @Override
    public void updateDescription() {
        // 更新描述文本，显示所选择的随从卡片
        description = DESCRIPTIONS[0] + getCardName(card) + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        // 在回合开始时调用
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) { // 如果敌人没有死亡
            flash(); // 闪烁效果
            AbstractCard toCreate = familiarFactory(card); // 根据选择的卡片类型创建随从卡片
            if (upgraded) { // 如果卡片已升级
                toCreate.upgrade(); // 升级卡片
                toCreate.upgraded = true; // 标记为已升级
            }
            // 创建该卡片并将其加入到玩家的手牌
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(toCreate, 1, false));
        }
    }

    private AbstractCard familiarFactory(FamiliarCardEnum card) {
        // 根据随从卡片类型返回相应的卡片对象
        switch (card) {
            case BAT:
                return new BatFamiliar(); // 蝙蝠随从卡片
            case CAT:
                return new CatFamiliar(); // 猫随从卡片
            case OWL:
                return new OwlFamiliar(); // 猫头鹰随从卡片
            case RAT:
                return new RatFamiliar(); // 老鼠随从卡片
            case TOAD:
                return new ToadFamiliar(); // 蟾蜍随从卡片
            case RAVEN:
                return new RavenFamiliar(); // 乌鸦随从卡片
        }

        return new CatFamiliar(); // 默认返回猫随从卡片
    }

    private String getCardName(FamiliarCardEnum card) {
        // 根据卡片类型获取其名称
        switch (card) {
            case BAT:
                return upgraded ? DESCRIPTIONS[2] : DESCRIPTIONS[3]; // 升级版或普通版的卡片名称
            case CAT:
                return upgraded ? DESCRIPTIONS[4] : DESCRIPTIONS[5];
            case OWL:
                return upgraded ? DESCRIPTIONS[6] : DESCRIPTIONS[7];
            case RAT:
                return upgraded ? DESCRIPTIONS[8] : DESCRIPTIONS[9];
            case TOAD:
                return upgraded ? DESCRIPTIONS[10] : DESCRIPTIONS[11];
            case RAVEN:
                return upgraded ? DESCRIPTIONS[12] : DESCRIPTIONS[13];
        }
        return "MISSING CARD " + card.toString(); // 如果没有匹配的卡片类型，返回缺失卡片的名称
    }

    public static AbstractCard getRandomFamiliarCard() {
        // 随机选择一个随从卡片
        switch ((int) (Math.random() * 6)) { // 随机生成一个0到5之间的数字
            case 0:
                return new BatFamiliar(); // 蝙蝠
            case 1:
                return new RatFamiliar(); // 老鼠
            case 2:
                return new OwlFamiliar(); // 猫头鹰
            case 3:
                return new ToadFamiliar(); // 蟾蜍
            case 4:
                return new RavenFamiliar(); // 乌鸦
            default:
                return new CatFamiliar(); // 默认猫
        }
    }
}
