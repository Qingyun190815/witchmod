package witchmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import witchmod.WitchMod;

public class TwistedMindPower extends AbstractWitchPower {
    // 能力的唯一标识符
    private static final String POWER_ID = "TwistedMindPower";

    // 能力的图标加载
    public static final String IMG = "powers/twistedmind.png";

    /**
     * TwistedMindPower 构造方法
     * @param owner 此能力的持有者
     * @param amount 每次造成的伤害量
     */
    public TwistedMindPower(AbstractCreature owner, int amount) {
        super(POWER_ID);  // 调用父类构造方法设置能力 ID

        // 设置能力的持有者为传入的 owner（目标）
        this.owner = owner;

        // 设置每次触发时造成的伤害量
        this.amount = amount;

        // 更新能力描述
        this.updateDescription();

        // 设置该能力不基于回合
        this.isTurnBased = false;

        // 加载能力的图标
        this.img = new Texture(WitchMod.getResourcePath(IMG));

        // 设置该能力的类型为增益效果（Buff）
        this.type = PowerType.BUFF;
    }

    /**
     * 更新能力的描述文本
     */
    @Override
    public void updateDescription() {
        // 描述该能力的效果，造成的伤害值
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    /**
     * 当玩家打出卡牌时触发的能力效果
     * 如果打出的卡牌费用为2或更高，则对所有敌人造成伤害
     * @param card 打出的卡牌
     * @param m 目标敌人
     */
    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        // 检查卡牌类型，如果是攻击、技能或能力卡
        if (card.type == CardType.ATTACK || card.type == CardType.SKILL || card.type == CardType.POWER) {
            // 如果卡牌费用大于或等于2
            if (card.costForTurn >= 2) {
                // 对所有敌人造成伤害
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(amount, true), DamageType.HP_LOSS, AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));

                // 闪烁效果，表示能力触发
                flash();
            }
        }
    }
}
