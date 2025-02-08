package witchmod.effects;

import com.badlogic.gdx.Gdx;  // 引入Gdx库，用于处理图形和游戏更新
import com.badlogic.gdx.graphics.Color;  // 引入颜色类，用于设置效果颜色
import com.badlogic.gdx.graphics.g2d.SpriteBatch;  // 引入SpriteBatch类，用于批量绘制图像
import com.badlogic.gdx.math.Interpolation;  // 引入插值类，用于平滑过渡效果
import com.badlogic.gdx.math.MathUtils;  // 引入数学工具类，用于生成随机数
import com.megacrit.cardcrawl.core.Settings;  // 引入游戏设置类，提供缩放等设置
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入游戏中的地牢类，用于控制效果队列
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;  // 引入抽象游戏效果类
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;  // 引入火焰爆发粒子效果
import com.megacrit.cardcrawl.vfx.GhostlyWeakFireEffect;  // 引入幽灵弱火粒子效果
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;  // 引入光晕粒子效果

/**
 * DarkboltEffect 类：实现一个黑暗闪电特效，带有火焰爆发、光晕和幽灵弱火效果。
 */
public class DarkboltEffect extends AbstractGameEffect {
    private static final float FIREBALL_INTERVAL = 0.016f;  // 控制粒子效果发射间隔（每16毫秒发射一次）
    private float x;  // 当前X坐标
    private float y;  // 当前Y坐标
    private float startX;  // 起始X坐标
    private float startY;  // 起始Y坐标
    private float targetX;  // 目标X坐标
    private float targetY;  // 目标Y坐标
    private float vfxTimer = 0.25f;  // 用于控制粒子效果的时间计时器（每隔0.25秒发射粒子）

    /**
     * 构造函数：初始化黑暗闪电特效的位置、目标位置和颜色
     *
     * @param startX 起始X坐标
     * @param startY 起始Y坐标
     * @param targetX 目标X坐标
     * @param targetY 目标Y坐标
     * @param color 特效的颜色
     */
    public DarkboltEffect(float startX, float startY, float targetX, float targetY, Color color) {
        this.startingDuration = 0.5f;  // 设置特效的持续时间为0.5秒
        this.duration = 0.5f;  // 初始化持续时间
        this.startX = startX;  // 设置起始X坐标
        this.startY = startY;  // 设置起始Y坐标
        // 给目标坐标添加随机偏移量，使效果略有不规则
        this.targetX = targetX + MathUtils.random(-20.0f, 20.0f) * Settings.scale;
        this.targetY = targetY + MathUtils.random(-20.0f, 20.0f) * Settings.scale;
        this.x = startX;  // 初始化当前X坐标
        this.y = startY;  // 初始化当前Y坐标
        this.color = color;  // 设置特效的颜色
    }

    /**
     * 构造函数：默认使用紫色作为黑暗闪电的颜色
     */
    public DarkboltEffect(float startX, float startY, float targetX, float targetY) {
        this(startX, startY, targetX, targetY, new Color(0.8f, 0.0f, 0.8f, 0.5f));  // 默认颜色为半透明的紫色
    }

    /**
     * 更新黑暗闪电特效的状态
     * - 更新位置（使用插值平滑过渡）
     * - 触发粒子效果
     * - 控制特效持续时间和结束时的附加效果
     */
    @Override
    public void update() {
        // 使用插值更新位置，使得特效的坐标从起始位置平滑过渡到目标位置
        this.x = Interpolation.fade.apply(this.targetX, this.startX, this.duration / this.startingDuration);
        this.y = Interpolation.fade.apply(this.targetY, this.startY, this.duration / this.startingDuration);

        // 更新粒子效果计时器，控制粒子发射的频率
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0f) {
            this.vfxTimer = FIREBALL_INTERVAL;  // 重置粒子发射计时器
            // 添加光晕和火焰爆发粒子效果到效果队列中
            AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(this.x, this.y, Color.BLACK));
            AbstractDungeon.effectsQueue.add(new FireBurstParticleEffect(this.x, this.y));
        }

        // 减少持续时间
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0f) {
            this.isDone = true;  // 特效完成
            // 特效结束时，添加点燃效果和幽灵弱火效果到效果队列中
            AbstractDungeon.effectsQueue.add(new IgniteEffect(this.x, this.y, color, 25));  // 点燃目标
            AbstractDungeon.effectsQueue.add(new GhostlyWeakFireEffect(this.x, this.y));  // 添加幽灵弱火效果
        }
    }

    /**
     * 渲染特效（目前不做任何渲染）
     *
     * @param sb SpriteBatch 用于批量渲染图像
     */
    @Override
    public void render(SpriteBatch sb) {
        // 当前不需要渲染任何额外的内容
    }

    /**
     * 清理资源
     */
    @Override
    public void dispose() {
        // 当前没有需要清理的资源
    }
}
