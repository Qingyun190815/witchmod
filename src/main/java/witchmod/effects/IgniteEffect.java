package witchmod.effects;

import com.badlogic.gdx.graphics.Color;  // 引入颜色类，用于设置粒子颜色
import com.badlogic.gdx.graphics.g2d.SpriteBatch;  // 引入SpriteBatch类，用于批量绘制图像
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入游戏中的地牢类，用于控制效果队列
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;  // 引入抽象游戏效果类
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;  // 引入火焰爆发粒子效果
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;  // 引入光晕粒子效果

/**
 * IgniteEffect 类：实现点燃效果，创建火焰爆发和光晕效果。
 */
public class IgniteEffect extends AbstractGameEffect {
    private int count = 25;  // 控制生成的粒子数量
    private float x;  // 火焰效果的X坐标
    private float y;  // 火焰效果的Y坐标

    /**
     * 构造函数：初始化火焰特效的位置、颜色和粒子数量
     *
     * @param x 火焰特效的X坐标
     * @param y 火焰特效的Y坐标
     * @param color 火焰特效的颜色
     * @param count 要生成的粒子数量
     */
    public IgniteEffect(float x, float y, Color color, int count) {
        this.x = x;  // 设置火焰特效的X坐标
        this.y = y;  // 设置火焰特效的Y坐标
        this.color = color;  // 设置火焰特效的颜色
        this.count = count;  // 设置火焰爆发生成的粒子数量
    }

    /**
     * 更新火焰效果
     * - 根据指定的粒子数量生成火焰爆发和光晕粒子效果
     */
    @Override
    public void update() {
        // 生成指定数量的火焰爆发和光晕粒子效果
        for (int i = 0; i < count; ++i) {
            AbstractDungeon.effectsQueue.add(new FireBurstParticleEffect(x, y));  // 添加火焰爆发效果
            AbstractDungeon.effectsQueue.add(new LightFlareParticleEffect(x, y, color));  // 添加光晕效果
        }
        // 完成后，标记该特效为已完成
        this.isDone = true;
    }

    /**
     * 渲染火焰效果（此类不需要额外的渲染）
     *
     * @param sb SpriteBatch 用于批量渲染图像
     */
    @Override
    public void render(SpriteBatch sb) {
        // 当前不需要渲染额外内容
    }

    /**
     * 清理资源（此类不需要额外的资源清理）
     */
    @Override
    public void dispose() {
        // 当前没有需要清理的资源
    }
}
