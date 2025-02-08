package witchmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class RustWall extends AbstractWitchCard {
    public static final String ID = "RustWall";  // 卡片ID
    public static final String NAME = "Wall of Rust";  // 卡片名称
    public static final String IMG = "cards/rustwall.png";  // 卡片图片路径
    public static final String DESCRIPTION = "Gain !B! block. Shuffle a Curse of Rust in your draw pile.";  // 卡片描述

    private static final CardRarity RARITY = CardRarity.RARE;  // 卡片稀有度
    private static final CardTarget TARGET = CardTarget.SELF;  // 目标：自己
    private static final CardType TYPE = CardType.SKILL;  // 卡片类型：技能

    private static final int POOL = 1;  // 池中的卡片数量

    private static final int COST = 0;  // 能量消耗
    private static final int POWER = 9;  // 基础护盾值
    private static final int UPGRADE_BONUS = 5;  // 升级后的护盾值增加量

    // 构造函数
    public RustWall() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        this.baseBlock = POWER;  // 设置基础护盾值
        cardPreviewTooltip = new RustWallCurse();  // 设置卡片预览效果为 Curse of Rust（锈蚀诅咒）
    }

    // 使用卡片时的效果
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 将一个 Curse of Rust 添加到抽牌堆中
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new RustWallCurse(), 1, true, false));
        // 为玩家增加护盾
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
    }

    // 创建卡片副本
    public AbstractCard makeCopy() {
        return new RustWall();  // 返回副本
    }

    // 升级效果
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeBlock(UPGRADE_BONUS);  // 增加护盾值
        }
    }
}
