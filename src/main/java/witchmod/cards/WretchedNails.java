package witchmod.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.CollectorStakeEffect;
import witchmod.WitchMod;

public class WretchedNails extends AbstractWitchCard {
    public static final String ID = "WretchedNails";  // 唯一ID
    public static final String NAME = "Wretched Nails";  // 卡牌名称
    public static final String IMG = "cards/wretchednails.png";  // 卡牌图片路径
    public static final String DESCRIPTION = "Draw 1 card, then deal damage equal to the number of cards you have drawn this turn.";  // 描述：抽取 1 张卡牌，然后造成伤害，伤害等于本回合抽取的卡牌数量。
    public static final String DESCRIPTION_UPGRADED = "Draw !M! cards, then deal damage equal to the number of cards you have drawn this turn.";  // 升级后的描述：抽取 !M! 张卡牌，然后造成伤害，伤害等于本回合抽取的卡牌数量。

    private static final CardRarity RARITY = CardRarity.COMMON;  // 稀有度：普通（COMMON）
    private static final CardTarget TARGET = CardTarget.ENEMY;  // 目标：敌人（ENEMY）
    private static final CardType TYPE = CardType.ATTACK;  // 卡牌类型：攻击（ATTACK）

    private static final int POOL = 1;  // 卡牌池大小
    private static final int COST = 1;  // 能量消耗：1 点

    private static final int MAGIC = 1;  // 初始抽卡数量
    private static final int MAGIC_UPGRADE_BONUS = 1;  // 升级后的额外抽卡数量

    public WretchedNails() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = MAGIC;  // 设置初始魔法数值
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获取本回合已抽取的卡牌数量
        int cardsDrawn = WitchMod.cardsDrawnThisTurn + magicNumber;

        // 显示视觉效果：每抽一张卡牌就生成一个效果
        for (int i = 0; i < cardsDrawn; i++) {
            AbstractDungeon.effectsQueue.add(new CollectorStakeEffect(m.hb.cX + MathUtils.random(-50.0f, 50.0f) * Settings.scale, m.hb.cY + MathUtils.random(-60.0f, 60.0f) * Settings.scale));
        }

        // 执行抽卡动作
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(1.25f));  // 等待一段时间以便效果展示

        // 执行伤害动作，伤害等于抽取的卡牌数
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, cardsDrawn, damageTypeForTurn), AbstractGameAction.AttackEffect.SMASH));

        // 更新 WitchMod 中的 cardsDrawnThisTurn
        WitchMod.cardsDrawnThisTurn += magicNumber;
    }

    @Override
    public AbstractCard makeCopy() {
        return new WretchedNails();
    }


    @Override
    public void applyPowers() {
        baseDamage = (int) calculateModifiedCardDamage(AbstractDungeon.player, 0);  // 根据本回合抽卡数计算伤害
        super.applyPowers();

        // 设置卡牌描述
        rawDescription = magicNumber != 1 ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;

        // 确保 EXTENDED_DESCRIPTION 数组的长度足够
        if (cardStrings.EXTENDED_DESCRIPTION.length > 0) {
            rawDescription = rawDescription + cardStrings.EXTENDED_DESCRIPTION[0];
        }
        rawDescription = rawDescription + damage;
        if (cardStrings.EXTENDED_DESCRIPTION.length > 1) {
            rawDescription = rawDescription + cardStrings.EXTENDED_DESCRIPTION[1];
        }
        initializeDescription();
    }


    @Override
    public void onMoveToDiscard() {
        rawDescription = magicNumber != 1 ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = (int) calculateModifiedCardDamage(AbstractDungeon.player, 0);  // 计算伤害
        super.calculateCardDamage(mo);

        // 更新卡牌描述
        rawDescription = magicNumber != 1 ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;

        // 确保 EXTENDED_DESCRIPTION 数组的长度足够
        if (cardStrings.EXTENDED_DESCRIPTION.length > 0) {
            rawDescription = rawDescription + cardStrings.EXTENDED_DESCRIPTION[0];
        }
        rawDescription = rawDescription + damage;
        if (cardStrings.EXTENDED_DESCRIPTION.length > 1) {
            rawDescription = rawDescription + cardStrings.EXTENDED_DESCRIPTION[1];
        }
        initializeDescription();
    }


    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, float tmp) {
        return WitchMod.cardsDrawnThisTurn + magicNumber;  // 伤害值等于本回合抽取的卡牌数量加上卡牌的魔法数值
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(MAGIC_UPGRADE_BONUS);  // 升级后增加抽卡数量
            upgradeDescription();
        }
    }
}
