package witchmod.effects;

import com.badlogic.gdx.Gdx;  // 引入Gdx类，用于访问游戏时间等功能
import com.badlogic.gdx.graphics.Color;  // 引入颜色类，用于设置特效颜色
import com.badlogic.gdx.graphics.g2d.SpriteBatch;  // 引入SpriteBatch类，用于批量绘制图像
import com.badlogic.gdx.graphics.g2d.TextureAtlas;  // 引入TextureAtlas类，用于加载图像资源
import com.badlogic.gdx.math.Interpolation;  // 引入插值类，用于计算效果动画的过渡
import com.badlogic.gdx.math.MathUtils;  // 引入数学工具类，用于随机数和角度计算
import com.megacrit.cardcrawl.core.CardCrawlGame;  // 引入游戏核心类，用于播放声音等
import com.megacrit.cardcrawl.core.Settings;  // 引入设置类，用于获取屏幕缩放比例
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入地牢类，用于添加游戏特效到队列
import com.megacrit.cardcrawl.helpers.ImageMaster;  // 引入图像资源加载器
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;  // 引入抽象游戏特效类
import com.megacrit.cardcrawl.vfx.combat.AdditiveSlashImpactEffect;  // 引入剑击特效类

/**
 * PunctureEffect 类：实现穿刺攻击的特效。
 */
public class PunctureEffect extends AbstractGameEffect {
    private static TextureAtlas.AtlasRegion img;  // 图像资源
    private float x;  // 特效起始位置X坐标
    private float y;  // 特效起始位置Y坐标
    private float sX;  // 起始X坐标
    private float sY;  // 起始Y坐标
    private float tX;  // 目标X坐标
    private float tY;  // 目标Y坐标
    private float targetAngle;  // 目标角度
    private float startingAngle;  // 初始角度
    private float targetScale;  // 目标缩放比例
    private boolean shownSlash = false;  // 标记是否已经显示了剑击效果

    /**
     * 构造函数：初始化穿刺特效的坐标、角度、缩放等参数。
     *
     * @param x 起始坐标X
     * @param y 起始坐标Y
     */
    public PunctureEffect(float x, float y) {
        if (img == null) {
            img = ImageMaster.vfxAtlas.findRegion("combat/stake");  // 获取穿刺效果的图像资源
        }
        float randomAngle = 0.017453292f * MathUtils.random(-50.0f, 230.0f);  // 随机生成一个角度
        this.x = MathUtils.cos(randomAngle) * MathUtils.random(200.0f, 400.0f) * Settings.scale + x;  // 计算起始位置
        this.y = MathUtils.sin(randomAngle) * MathUtils.random(200.0f, 400.0f) * Settings.scale + y;
        this.duration = 0.5f;  // 持续时间
        this.scale = 0.01f;  // 初始缩放比例
        this.targetScale = MathUtils.random(0.1f, 0.5f);  // 随机目标缩放比例
        this.targetAngle = MathUtils.atan2(y - this.y, x - this.x) * 57.295776f + 90.0f;  // 计算目标角度
        this.rotation = this.startingAngle = MathUtils.random(0.0f, 360.0f);  // 随机初始角度
        this.x -= (float)(PunctureEffect.img.packedWidth / 2);  // 调整X坐标，确保中心对齐
        this.y -= (float)(PunctureEffect.img.packedHeight / 2);  // 调整Y坐标，确保中心对齐
        this.sX = this.x;  // 保存起始X坐标
        this.sY = this.y;  // 保存起始Y坐标
        this.tX = x - (float)(PunctureEffect.img.packedWidth / 2);  // 目标X坐标
        this.tY = y - (float)(PunctureEffect.img.packedHeight / 2);  // 目标Y坐标
        this.color = new Color(0.2f, 0.2f, 0.2f, 1.0f);  // 设置颜色
    }

    /**
     * 更新穿刺效果的位置、角度、缩放等属性。
     */
    @Override
    public void update() {
        // 使用插值调整旋转角度
        this.rotation = Interpolation.elasticIn.apply(this.targetAngle, this.startingAngle, this.duration);
        if (this.duration > 0.5f) {
            // 逐步缩放，并逐渐改变透明度
            this.scale = Interpolation.elasticIn.apply(this.targetScale, this.targetScale * 10.0f, (this.duration - 0.5f) * 2.0f) * Settings.scale;
            this.color.a = Interpolation.fade.apply(1.0f, 0.5f, (this.duration - 0.5f) * 2.0f);
        } else {
            // 使用插值逐渐逼近目标位置
            this.x = Interpolation.exp10Out.apply(this.tX, this.sX, this.duration * 2.0f);
            this.y = Interpolation.exp10Out.apply(this.tY, this.sY, this.duration * 2.0f);
        }
        // 在最后的时间段，显示剑击特效
        if (this.duration < 0.05f && !this.shownSlash) {
            AbstractDungeon.effectsQueue.add(new AdditiveSlashImpactEffect(this.tX + (float)PunctureEffect.img.packedWidth / 2.0f, this.tY + (float)PunctureEffect.img.packedHeight / 2.0f, this.color.cpy()));
            this.shownSlash = true;
        }
        this.duration -= Gdx.graphics.getDeltaTime();  // 减少持续时间
        if (this.duration < 0.0f) {
            this.isDone = true;  // 特效完成
            CardCrawlGame.sound.play("ATTACK_FAST", 0.2f);  // 播放攻击音效
        }
    }

    /**
     * 渲染穿刺效果的图像。
     *
     * @param sb SpriteBatch 用于批量渲染图像
     */
    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);  // 设置混合模式
        sb.setColor(this.color);  // 设置渲染颜色
        // 绘制穿刺效果图像
        sb.draw(img, this.x, this.y, (float)PunctureEffect.img.packedWidth / 2.0f, (float)PunctureEffect.img.packedHeight / 2.0f, PunctureEffect.img.packedWidth, PunctureEffect.img.packedHeight, this.scale * MathUtils.random(1.0f, 1.2f), this.scale * MathUtils.random(1.0f, 1.2f), this.rotation);
        sb.draw(img, this.x, this.y, (float)PunctureEffect.img.packedWidth / 2.0f, (float)PunctureEffect.img.packedHeight / 2.0f, PunctureEffect.img.packedWidth, PunctureEffect.img.packedHeight, this.scale * MathUtils.random(0.9f, 1.1f), this.scale * MathUtils.random(0.9f, 1.1f), this.rotation);
        sb.setBlendFunction(770, 771);  // 恢复混合模式
    }

    @Override
    public void dispose() {
        // 目前不需要清理资源
    }
}
