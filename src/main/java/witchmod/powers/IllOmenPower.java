package witchmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.VerticalAuraEffect;

import witchmod.WitchMod;

/**
 * IllOmenPower 类表示一个当玩家抽到诅咒卡片时，对所有敌人造成伤害的能力。
 * 一旦玩家抽到诅咒卡，触发一次性效果。
 */
public class IllOmenPower extends AbstractWitchPower {
    // 完整的能力 ID，用于标识此能力
    private static final String POWER_ID = "IllOmenPower";

    // 能力图标的路径，用于显示此能力的图像
    public static final String IMG = "powers/illomen.png";

    // 标记是否已经触发过效果
    private boolean triggered = false;

    /**
     * IllOmenPower 的构造方法。
     * @param owner 此能力的持有者（通常为玩家）
     * @param amount 能力强度，决定对敌人造成的伤害值
     */
    public IllOmenPower(AbstractCreature owner, int amount) {
        super(POWER_ID);  // 调用父类构造方法设置能力 ID

        // 设置能力的持有者为传入的 owner（玩家或其他角色）
        this.owner = owner;

        // 设置能力的强度（amount），决定伤害值
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
     */
    @Override
    public void updateDescription() {
        // 格式化描述，显示下一次抽到诅咒卡时造成的伤害
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    /**
     * 当玩家抽到卡牌时检查是否为诅咒卡，如果是，则触发能力效果。
     * @param card 玩家抽到的卡牌
     */
    @Override
    public void onCardDraw(AbstractCard card) {
        // 如果抽到的是诅咒卡，并且该效果尚未触发，则触发该效果
        if (card.type == CardType.CURSE && !triggered) {
            // 触发闪烁效果，表示能力激活
            flash();

            // 标记为已触发
            triggered = true;

            // 移除 IllOmenPower
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, ID));

            // 播放视觉效果，表示诅咒的降临
            AbstractDungeon.actionManager.addToTop(new VFXAction(AbstractDungeon.player,
                    new VerticalAuraEffect(Color.BLACK, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1f));

            // 对所有敌人造成伤害，伤害值为 amount
            AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(owner,
                    DamageInfo.createDamageMatrix(amount, true), DamageType.THORNS, AttackEffect.POISON));
        }
    }
}
