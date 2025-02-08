package witchmod.powers;

import java.util.List;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class TrollsBloodPower extends AbstractWitchPower {
    // 能力的唯一标识符
    private static final String POWER_ID = "TrollsBloodPower";

    // 能力的图标加载
    public static final String IMG = "powers/regen.png";

    /**
     * TrollsBloodPower 构造方法
     * @param owner 此能力的持有者
     * @param regenAmt 恢复的生命值
     */
    public TrollsBloodPower(AbstractCreature owner, int regenAmt) {
        super(POWER_ID);  // 调用父类构造方法设置能力 ID

        // 设置能力的持有者为传入的 owner（目标）
        this.owner = owner;

        // 设置每回合恢复的生命值
        this.amount = regenAmt;

        // 更新能力描述
        this.updateDescription();

        // 设置图标，加载生命恢复图标
        this.loadRegion("regen");

        // 设置该能力的类型为增益效果（Buff）
        this.type = AbstractPower.PowerType.BUFF;
    }

    /**
     * 更新能力的描述文本
     */
    @Override
    public void updateDescription() {
        // 描述能力的效果，恢复生命的数值
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    /**
     * 在回合结束时触发的能力效果
     * 如果在本回合内有打出攻击卡，则恢复一定的生命值
     * @param isPlayer 是否是玩家的回合
     */
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        // 获取本回合已打出的卡牌列表
        List<AbstractCard> cards = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        // 遍历已打出的卡牌，如果有攻击卡，则恢复生命
        for (AbstractCard card : cards) {
            if (card.type == CardType.ATTACK) {
                flash();  // 闪烁提示，表示效果生效
                // 恢复生命
                AbstractDungeon.actionManager.addToBottom(new HealAction(owner, owner, amount));
                return;  // 找到攻击卡后，恢复生命并跳出循环
            }
        }
    }
}
