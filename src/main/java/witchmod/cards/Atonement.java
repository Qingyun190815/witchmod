package witchmod.cards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.List;

public class Atonement extends AbstractWitchCard {
    public static final String ID = "Atonement";  // 卡片ID
    public static final String NAME = "赎罪";  // 卡片名称
    public static final String IMG = "cards/atonement.png";  // 卡片图片路径
    public static final String DESCRIPTION = "消耗抽牌堆中的一张随机状态或诅咒卡. NL 获得 !B! 点格挡.";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.COMMON;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int COST = 1;  // 消耗能量
    private static final int POWER = 7;  // 默认护甲量
    private static final int POWER_UPGRADED = 4;  // 升级后的护甲量增益

    public Atonement() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置基础护甲
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 获取抽牌堆中的随机状态卡或诅咒卡
        AbstractCard toExhaust = getRandomCurseOrStatusFromDeck();
        if (toExhaust != null) {
            // 设置卡片动画效果
            toExhaust.current_y = -200.0f * Settings.scale;
            toExhaust.target_x = Settings.WIDTH / 2.0f;
            toExhaust.target_y = Settings.HEIGHT / 2.0f;
            toExhaust.targetAngle = 0.0f;
            toExhaust.lighten(false);
            toExhaust.drawScale = 0.12f;
            toExhaust.targetDrawScale = 0.75f;

            // 添加消耗卡片的动作
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.5f));  // 等待时间
            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(toExhaust, p.drawPile));
        }

        // 增加护甲
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Atonement();  // 返回一张新的卡片副本
    }

    // 获取随机的状态卡或诅咒卡
    private AbstractCard getRandomCurseOrStatusFromDeck() {
        List<AbstractCard> candidates = new ArrayList<>();

        // 优先选择状态卡
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.type == CardType.STATUS) {
                candidates.add(c);
            }
        }

        // 如果没有状态卡，选择普通诅咒卡
        if (candidates.size() == 0) {
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c.type == CardType.CURSE && !(c instanceof AbstractWitchCleansableCurse)) {
                    candidates.add(c);
                }
            }
        }

        // 如果没有普通诅咒卡，选择可清除的诅咒卡
        if (candidates.size() == 0) {
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c.type == CardType.CURSE) {
                    candidates.add(c);
                }
            }
        }

        // 如果没有可消耗的卡片，则返回null
        if (candidates.size() == 0) {
            return null;
        }

        // 随机选择一张卡片
        return candidates.get(MathUtils.random(candidates.size() - 1));
    }

    // 升级卡片
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(POWER_UPGRADED);  // 增加护甲值
        }
    }
}
