package witchmod.effects;

import com.badlogic.gdx.graphics.Color;  // 引入颜色类，用于设置震荡波的颜色
import com.badlogic.gdx.graphics.g2d.SpriteBatch;  // 引入SpriteBatch类，用于批量绘制图像
import com.badlogic.gdx.math.MathUtils;  // 引入数学工具类，用于生成随机数
import com.megacrit.cardcrawl.core.CardCrawlGame;  // 引入游戏核心类，处理屏幕震动
import com.megacrit.cardcrawl.core.Settings;  // 引入游戏设置类，提供缩放等设置
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;  // 引入游戏中的地牢类，用于控制效果队列
import com.megacrit.cardcrawl.helpers.ScreenShake;  // 引入屏幕震动效果
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;  // 引入抽象游戏效果类
import com.megacrit.cardcrawl.vfx.combat.BlurWaveAdditiveEffect;  // 引入Additive类型震荡波效果
import com.megacrit.cardcrawl.vfx.combat.BlurWaveChaoticEffect;  // 引入Chaotic类型震荡波效果
import com.megacrit.cardcrawl.vfx.combat.BlurWaveNormalEffect;  // 引入Normal类型震荡波效果

/**
 * FastShockWaveEffect 类：实现一个快速震荡波特效，具有不同的震荡波类型。
 */
public class FastShockWaveEffect extends AbstractGameEffect {
    private float x;  // 震荡波中心X坐标
    private float y;  // 震荡波中心Y坐标
    private ShockWaveType type;  // 震荡波的类型（Additive、Normal、Chaotic）
    private Color color;  // 震荡波的颜色

    /**
     * 构造函数：初始化震荡波的坐标、颜色和类型
     *
     * @param x 中心X坐标
     * @param y 中心Y坐标
     * @param color 震荡波的颜色
     * @param type 震荡波的类型
     */
    public FastShockWaveEffect(float x, float y, Color color, ShockWaveType type) {
        this.x = x;  // 设置震荡波的X坐标
        this.y = y;  // 设置震荡波的Y坐标
        this.type = type;  // 设置震荡波的类型
        this.color = color;  // 设置震荡波的颜色
    }

    /**
     * 更新震荡波效果
     * - 随机生成震荡波速度
     * - 根据震荡波的类型，添加对应的震荡波效果到效果队列
     * - 结束后将特效标记为完成
     */
    @Override
    public void update() {
        // 随机生成震荡波的速度，范围在2000到3000之间，并应用游戏的缩放设置
        float speed = MathUtils.random(2000.0f, 3000.0f) * Settings.scale;

        // 根据震荡波的类型，添加对应的震荡波效果
        switch (this.type) {
            case ADDITIVE: {
                // 如果类型为 ADDITIVE，添加40个 Additive 类型的震荡波效果
                for (int i = 0; i < 40; ++i) {
                    AbstractDungeon.effectsQueue.add(new BlurWaveAdditiveEffect(this.x, this.y, this.color.cpy(), speed));
                }
                break;
            }
            case NORMAL: {
                // 如果类型为 NORMAL，添加40个 Normal 类型的震荡波效果
                for (int i = 0; i < 40; ++i) {
                    AbstractDungeon.effectsQueue.add(new BlurWaveNormalEffect(this.x, this.y, this.color.cpy(), speed));
                }
                break;
            }
            case CHAOTIC: {
                // 如果类型为 CHAOTIC，触发高强度的屏幕震动效果
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.SHORT, false);
                // 添加40个 Chaotic 类型的震荡波效果
                for (int i = 0; i < 40; ++i) {
                    AbstractDungeon.effectsQueue.add(new BlurWaveChaoticEffect(this.x, this.y, this.color.cpy(), speed));
                }
                break;
            }
        }

        // 完成后，标记特效为已完成
        this.isDone = true;
    }

    /**
     * 渲染震荡波效果（此类不需要额外的渲染）
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

    /**
     * 震荡波类型枚举：定义三种震荡波类型
     */
    public static enum ShockWaveType {
        ADDITIVE,  // 加法型震荡波
        NORMAL,    // 普通型震荡波
        CHAOTIC;   // 混沌型震荡波

        private ShockWaveType() {
            // 默认构造函数
        }
    }
}
