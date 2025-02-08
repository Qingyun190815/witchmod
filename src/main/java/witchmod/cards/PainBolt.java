package witchmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.PaperFrog;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

public class PainBolt extends AbstractWitchCard {
    public static final String ID = "PainBolt";
    public static final String NAME = "Pain Bolt";
    public static final String IMG = "cards/painbolt.png";
    public static final String DESCRIPTION = "施加 X 脆弱状态,然后造成 !D! X 伤害.";
    public static final String[] EXTENDED_DESCRIPTION = new String[]{"   NL   当前伤害: "};

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;

    private static final int POOL = 1;

    private static final int COST = -1;
    private static final int POWER = 4;
    private static final int UPGRADE_BONUS = 2;

    public PainBolt() {
        super(ID, IMG, COST, TYPE, RARITY, TARGET);
        baseDamage = POWER;  // 设置基础伤害
    }

    // 卡片使用时的逻辑
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.getCurrentEnergy();  // 获取当前能量
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SCARLET)));  // 特效：边框闪烁
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY), 0.3f));  // 特效：小型激光

        if (effect > 0) {  // 如果能量大于0
            // 对敌人施加 Vulnerable 效果，增加敌人所受伤害
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new VulnerablePower(m, effect, false), effect));

            // 如果敌人没有 Vulnerable 效果并且没有 Artifact 效果
            if (!m.hasPower(VulnerablePower.POWER_ID) && !m.hasPower(ArtifactPower.POWER_ID)) {
                // 根据是否拥有 PaperFrog 遗物来增加伤害
                damage *= p.hasRelic(PaperFrog.ID) ? 1.75 : 1.5;
            }
            // 计算伤害并进行攻击
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage * effect, damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        }
        p.energy.use(effect);  // 使用消耗的能量
    }

    // 应用卡片的属性（比如添加 Vulnerable 状态的伤害增益）
    @Override
    public void applyPowers() {
        super.applyPowers();
        int effect = EnergyPanel.getCurrentEnergy();  // 获取当前能量
        int totalDamage = effect * damage;  // 计算总伤害
        rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + totalDamage;  // 更新描述
        initializeDescription();  // 初始化卡片描述
    }

    // 计算卡片的伤害时的逻辑
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo);
        int effect = EnergyPanel.getCurrentEnergy();  // 获取当前能量
        int totalDamage = effect * damage;  // 计算总伤害

        // 如果敌人没有 Vulnerable 和 Artifact 效果，增加伤害
        if (mo != null && !mo.hasPower(VulnerablePower.POWER_ID) && !mo.hasPower(ArtifactPower.POWER_ID)) {
            totalDamage *= AbstractDungeon.player.hasRelic(PaperFrog.ID) ? 1.75 : 1.5;
        }

        // 更新描述
        rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0] + totalDamage;
        initializeDescription();  // 初始化描述
    }

    // 卡片移动到弃牌堆时的行为
    @Override
    public void onMoveToDiscard() {
        rawDescription = DESCRIPTION;  // 重置描述
        initializeDescription();  // 初始化描述
    }

    // 生成卡片副本
    public AbstractCard makeCopy() {
        return new PainBolt();  // 返回副本
    }

    // 升级卡片的行为
    public void upgrade() {
        if (!upgraded) {
            upgradeName();  // 升级卡片名称
            upgradeDamage(UPGRADE_BONUS);  // 升级卡片伤害
        }
    }
}
