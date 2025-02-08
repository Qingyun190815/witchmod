package witchmod.cards.familiar;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import witchmod.cards.AbstractWitchCard;

public class OwlFamiliar extends AbstractWitchCard {
	// 卡片的ID、名称、图片和描述
	public static final String ID = "OwlFamiliar";  // 卡片的唯一标识符
	public static final String NAME = "猫头鹰";  // 卡片名称
	public static final String IMG = "cards/owl.png";  // 卡片的图片路径
	public static final String DESCRIPTION = "虚无. NL 抽 !M! 张牌. NL 耗尽.";  // 卡片描述，描述卡片效果

	// 卡片的稀有度、目标和类型
	private static final CardRarity RARITY = CardRarity.SPECIAL;  // 卡片稀有度为特殊
	private static final CardTarget TARGET = CardTarget.SELF;  // 目标是自己
	private static final CardType TYPE = CardType.SKILL;  // 卡片类型是技能卡

	// 初始卡片数值
	private static final int POOL = 0;  // 该卡不属于任何池子
	private static final int COST = 1;  // 卡片的费用是1
	private static final int POWER = 2;  // 卡片的初始魔法值（即每次使用抽取的卡片数量）
	private static final int UPGRADE_BONUS = 1;  // 卡片升级后的增益，每次升级增加抽卡数量

	// 构造函数，初始化卡片的基本属性
	public OwlFamiliar() {
		super(ID, IMG, COST, TYPE, RARITY, TARGET);
		this.magicNumber = this.baseMagicNumber = POWER;  // 设置初始魔法值为2
		this.exhaust = true;  // 卡片使用后会被弃置
		this.isEthereal = true;  // 卡片是虚无的，表示使用后会被移除并在回合结束时被弃置
	}

	// 使用卡片时执行的操作
	public void use(AbstractPlayer p, AbstractMonster m) {
		// 执行抽卡动作，抽取 magicNumber 张卡片（初始为2）
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
	}

	// 克隆卡片，生成一张新的相同卡片
	public AbstractCard makeCopy() {
		return new OwlFamiliar();
	}

	// 玩家回合结束时触发的行为
	@Override
	public void triggerOnEndOfPlayerTurn() {
		// 执行弃置所有虚无卡片的操作
		AbstractDungeon.actionManager.addToTop(new ExhaustAllEtherealAction());
	}

	// 升级卡片，增加其抽卡数量
	public void upgrade() {
		if (!upgraded) {
			// 升级卡片名称
			upgradeName();
			// 升级魔法值（增加抽卡数量）
			upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
