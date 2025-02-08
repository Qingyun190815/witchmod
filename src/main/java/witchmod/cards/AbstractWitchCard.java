package witchmod.cards;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import witchmod.WitchMod;
import witchmod.patches.AbstractCardEnum;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractWitchCard extends CustomCard {
    // 是否在使用后重新洗牌，默认为 false
    public boolean reshuffleOnUse = false;
    // 是否在手牌中被丢弃时重新洗牌，默认为 false
    public boolean reshuffleOnDiscardFromHand = false;
    // 卡片预览提示
    protected AbstractCard cardPreviewTooltip;
    // 卡片扩展描述
    protected String[] EXTENDED_DESCRIPTION;
    // 存储卡片字符串信息
    protected CardStrings cardStrings;

    // 构造函数，通过卡片ID, 图片路径, 费用, 类型, 稀有度, 目标类型进行初始化
    public AbstractWitchCard(final String id,
                             String img,
                             final int cost,
                             final CardType type,
                             final CardRarity rarity,
                             final CardTarget target) {
        this("WitchMod:"+id, languagePack.getCardStrings("WitchMod:"+id), img, cost, type, rarity, target);
    }

    // 私有构造函数，初始化卡片相关信息
    private AbstractWitchCard(String id,
                              CardStrings cardStrings,
                              String img,
                              int cost,
                              CardType type,
                              CardRarity rarity,
                              CardTarget target) {
        super(id, cardStrings.NAME, WitchMod.getResourcePath(img), cost, cardStrings.DESCRIPTION, type, AbstractCardEnum.WITCH, rarity, target);
        this.cardStrings = cardStrings; // 设置卡片字符串
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION; // 设置卡片扩展描述
    }

    // 渲染卡片（显示在屏幕上）
    @Override
    public void render(SpriteBatch sb, boolean selected) {
        if (!Settings.hideCards) {
            // 如果卡片有闪光效果，则渲染闪光效果
            if (this.flashVfx != null) {
                this.flashVfx.render(sb);
            }
            try {
                // 获取"hovered"字段，用于检查卡片是否被悬停
                Field f = AbstractCard.class.getDeclaredField("hovered");
                f.setAccessible(true);
                this.renderCard(sb, (boolean) f.get(this), selected); // 渲染卡片
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

            // 渲染卡片的边框
            this.hb.render(sb);
        }
    }

    // 渲染卡片升级预览
    @Override
    public void renderUpgradePreview(SpriteBatch sb) {
        this.upgraded = true;
        this.name = this.originalName + "+"; // 加上+表示升级
        this.initializeTitle(); // 初始化卡片标题

        try {
            Field f = AbstractCard.class.getDeclaredField("hovered");
            f.setAccessible(true);
            this.renderCard(sb, (boolean) f.get(this), false); // 渲染卡片
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // 恢复原始名称，重置卡片状态
        this.name = this.originalName;
        this.initializeTitle();
        this.upgraded = false;
        this.resetAttributes(); // 重置属性
    }

    // 渲染卡片带选择框的状态
    @Override
    public void renderWithSelections(SpriteBatch sb) {
        this.renderCard(sb, false, true); // 渲染卡片并显示为选中状态
    }


//    updateGlowMethod：更新卡片的发光效果。
//    renderGlowMethod：渲染卡片的发光效果。
//    renderImageMethod：渲染卡片的图像。
//    renderTitleMethod：渲染卡片的标题。
//    renderTypeMethod：渲染卡片的类型（例如，技能、攻击等）。
//    renderTintMethod：渲染卡片的色调。
//    renderEnergyMethod：渲染卡片的能量消耗。
//    renderBackMethod：渲染卡片的背面（如果卡片是翻转的）。
    // 渲染卡片的详细信息，使用反射调用原卡片类中的私有方法
    private void renderCard(SpriteBatch sb, boolean hovered, boolean selected) {
        try {
            Method isOnScreenMethod = AbstractCard.class.getDeclaredMethod("isOnScreen");
            isOnScreenMethod.setAccessible(true);
            Method updateGlowMethod = AbstractCard.class.getDeclaredMethod("updateGlow");
            updateGlowMethod.setAccessible(true);
            Method renderGlowMethod = AbstractCard.class.getDeclaredMethod("renderGlow", SpriteBatch.class);
            renderGlowMethod.setAccessible(true);
            Method renderImageMethod = AbstractCard.class.getDeclaredMethod("renderImage", SpriteBatch.class, boolean.class, boolean.class);
            renderImageMethod.setAccessible(true);
            Method renderTitleMethod = AbstractCard.class.getDeclaredMethod("renderTitle", SpriteBatch.class);
            renderTitleMethod.setAccessible(true);
            Method renderTypeMethod = AbstractCard.class.getDeclaredMethod("renderType", SpriteBatch.class);
            renderTypeMethod.setAccessible(true);
            Method renderTintMethod = AbstractCard.class.getDeclaredMethod("renderTint", SpriteBatch.class);
            renderTintMethod.setAccessible(true);
            Method renderEnergyMethod = AbstractCard.class.getDeclaredMethod("renderEnergy", SpriteBatch.class);
            renderEnergyMethod.setAccessible(true);
            Method renderBackMethod = AbstractCard.class.getDeclaredMethod("renderBack", SpriteBatch.class, boolean.class, boolean.class);
            renderBackMethod.setAccessible(true);

            if (!Settings.hideCards) {
                if (!(boolean) isOnScreenMethod.invoke(this)) {
                    return; // 如果卡片不在屏幕上，则不渲染
                }
                if (!this.isFlipped) {
                    // 渲染卡片的效果和图像
                    updateGlowMethod.invoke(this);
                    renderGlowMethod.invoke(this, sb);
                    if (type == CardType.CURSE) { // 如果是诅咒类型，使用黑色背景
                        color = CardColor.CURSE;
                    }
                    renderImageMethod.invoke(this, sb, hovered, selected);
                    if (type == CardType.CURSE) {
                        color = AbstractCardEnum.WITCH;
                    }
                    renderTypeMethod.invoke(this, sb);
                    renderTitleMethod.invoke(this, sb);
                    if (Settings.lineBreakViaCharacter) {
                        renderDescriptionCN(sb); // 渲染卡片描述（中文）
                    } else {
                        renderDescription(sb); // 渲染卡片描述
                    }
                    renderTintMethod.invoke(this, sb);
                    renderEnergyMethod.invoke(this, sb);
                } else {
                    renderBackMethod.invoke(this, sb, hovered, selected);
                    hb.render(sb);
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // 渲染中文描述
// 这是一个受保护的方法，用于渲染卡片的中文描述
    protected void renderDescriptionCN(SpriteBatch sb) {
        try {
            // 通过反射获取 AbstractCard 类中的 renderDescriptionCN 方法
            // 该方法的参数是一个 SpriteBatch 对象
            Method renderDescriptionCNMethod = AbstractCard.class.getDeclaredMethod("renderDescriptionCN", SpriteBatch.class);
            // 设置该方法为可访问，因为 renderDescriptionCN 可能是一个私有方法
            renderDescriptionCNMethod.setAccessible(true);
            // 调用获取到的 renderDescriptionCN 方法，并传入当前对象 (this) 和 sb 作为参数
            renderDescriptionCNMethod.invoke(this, sb);
        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            // 如果反射过程中发生异常，打印异常堆栈信息
            e.printStackTrace();
        }
    }


    // 渲染卡片的描述
    protected void renderDescription(SpriteBatch sb) {
        try {
            Method renderDescriptionMethod = AbstractCard.class.getDeclaredMethod("renderDescription", SpriteBatch.class);
            renderDescriptionMethod.setAccessible(true);
            renderDescriptionMethod.invoke(this, sb);
        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renderCardTip(SpriteBatch sb) {
        // 调用父类的 renderCardTip 方法，确保父类的渲染逻辑被执行
        super.renderCardTip(sb);

        // 使用反射获取 AbstractCard 类中的私有字段 "renderTip"，判断是否需要渲染提示
        boolean renderTip = ReflectionHacks.getPrivate(this, AbstractCard.class, "renderTip");

        // 如果卡片有预览提示（cardPreviewTooltip 不为空），且未隐藏卡片（!Settings.hideCards），且需要渲染提示（renderTip 为 true）
        if (cardPreviewTooltip != null && !Settings.hideCards && renderTip) {
            // 如果玩家正在拖拽卡片，则不显示提示框
            if (AbstractDungeon.player != null && AbstractDungeon.player.isDraggingCard) {
                return;
            }

            // 计算提示框的水平和垂直偏移量
            float dx = (AbstractCard.IMG_WIDTH / 1.2f + 16f) * drawScale; // 水平偏移量
            float dy = (AbstractCard.IMG_WIDTH / 4f) * drawScale;        // 垂直偏移量

            // 根据卡片的当前 x 坐标，决定提示框显示在卡片的左侧还是右侧
            if (current_x > Settings.WIDTH * 0.75f) {
                // 如果卡片的 x 坐标大于屏幕宽度的 75%，提示框显示在卡片右侧
                cardPreviewTooltip.current_x = current_x + dx;
            } else {
                // 否则，提示框显示在卡片左侧
                cardPreviewTooltip.current_x = current_x - dx;
            }

            // 设置提示框的 y 坐标和缩放比例
            cardPreviewTooltip.current_y = current_y + dy;
            cardPreviewTooltip.drawScale = drawScale / 1.5f;

            // 渲染提示框
            cardPreviewTooltip.render(sb);
        }
    }


    // 升级卡片时更新描述
    public void upgradeDescription() {
        String upgradeDescription = languagePack.getCardStrings(cardID).UPGRADE_DESCRIPTION;
        if (upgradeDescription != null) {
            rawDescription = upgradeDescription; // 设置升级后的描述
            initializeDescription(); // 初始化描述
        }
    }

    // 在卡片被抽到时触发
    public void triggerWhenDraw(){
    unveil();
    }


    public void unveil() {

    }
}
