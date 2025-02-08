package witchmod.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import witchmod.WitchMod;

import java.util.ArrayList;

public abstract class AbstractWitchPower extends AbstractPower {
    // 初始化日志记录器，用于记录调试信息
    public static final Logger logger = LogManager.getLogger(WitchMod.class);

    // 用于渲染图标时的颜色，可以自定义
    protected Color renderColor = null;

    // 用于存储技能的描述文本，支持本地化
    protected final String[] DESCRIPTIONS;

    // 构造函数，初始化技能的ID、名称和描述
    public AbstractWitchPower(String id) {
        // 设置技能的唯一ID，格式为"ModID:skillID"
        this.ID = "WitchMod:" + id;

        // 加载技能的本地化名称和描述文本
        PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(ID);

        // 设置技能的名称
        name = powerStrings.NAME;

        // 设置技能的描述数组
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;

        // 打印日志，记录技能描述加载的情况
        logger.info("Loading power desc for " + ID + ": " + name + " " + DESCRIPTIONS[0]);
    }

    // 空方法：可以在子类中重写，用于处理每次抽卡时的行为
    public void onCardDraw(AbstractCard card) {
    }

    // 空方法：可以在子类中重写，用于处理护盾破裂时的行为
    public void onBlockBreak() {
    }

    // 渲染技能图标的方法
    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        // 如果 renderColor 为 null，使用传入的默认颜色 c
        if (renderColor == null) {
            sb.setColor(c);
        } else {
            // 否则使用自定义的 renderColor
            sb.setColor(renderColor);
        }

        // 如果技能有自定义的图标 (img)，则绘制该图标
        if (img != null) {
            sb.draw(img, x - 12.0f, y - 12.0f, 16.0f, 16.0f, 32.0f, 32.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 32, 32, false, false);
        } else {
            // 如果没有自定义图标，使用默认的 region48 进行渲染
            sb.draw(region48, x - (float) region48.packedWidth / 2.0f, y - (float) region48.packedHeight / 2.0f,
                    (float) region48.packedWidth / 2.0f, (float) region48.packedHeight / 2.0f,
                    region48.packedWidth, region48.packedHeight, Settings.scale, Settings.scale, 0.0f);
        }

        // 使用反射获取技能效果列表（这是一个私有字段），并渲染它们
        ArrayList<AbstractGameEffect> effectList = ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");
        for (AbstractGameEffect e : effectList) {
            // 渲染效果
            e.render(sb, x, y);
        }
    }
}
