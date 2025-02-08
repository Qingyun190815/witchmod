package witchmod.effects;

import com.badlogic.gdx.Gdx;  // 引入Gdx库，提供与游戏引擎的交互
import com.badlogic.gdx.graphics.Color;  // 引入颜色类，用于设置效果颜色
import com.badlogic.gdx.graphics.g2d.SpriteBatch;  // 引入SpriteBatch类，用于批量渲染图像
import com.badlogic.gdx.graphics.g2d.TextureAtlas;  // 引入纹理图集类，用于加载图像
import com.badlogic.gdx.math.Interpolation;  // 引入插值类，用于创建平滑过渡效果
import com.badlogic.gdx.math.MathUtils;  // 引入数学工具类，用于生成随机数
import com.megacrit.cardcrawl.core.CardCrawlGame;  // 引入游戏核心类，播放声音等
import com.megacrit.cardcrawl.core.Settings;  // 引入游戏设置类，提供缩放等
import com.megacrit.cardcrawl.helpers.ImageMaster;  // 引入图像助手类，提供默认图像
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;  // 引入游戏效果的抽象基类

/**
 * ColoredSliceEffect 类：实现一个彩色的切割效果，带有渐变的透明度和随机音效。
 */
public class ColoredSliceEffect extends AbstractGameEffect {
	private float x;  // 当前的X坐标
	private float startX;  // 初始的X坐标
	private float y;  // 当前的Y坐标
	private float startY;  // 初始的Y坐标
	private float targetX;  // 目标X坐标
	private float targetY;  // 目标Y坐标
	private static final float DUR = 0.4f;  // 持续时间（0.4秒）
	private TextureAtlas.AtlasRegion img = ImageMaster.DAGGER_STREAK;  // 使用默认的匕首特效图像
	private boolean playedSound = false;  // 标志位，用来确保音效只播放一次

	/**
	 * 构造函数：初始化彩色切割效果的位置、目标位置、颜色等参数
	 *
	 * @param x 起始X坐标
	 * @param y 起始Y坐标
	 * @param targetX 目标X坐标
	 * @param targetY 目标Y坐标
	 * @param color 特效的颜色
	 */
	public ColoredSliceEffect(float x, float y, float targetX, float targetY, Color color) {
		this.x = this.startX = x;  // 设置初始X坐标
		this.y = this.startY = y;  // 设置初始Y坐标
		this.targetX = targetX;  // 设置目标X坐标
		this.targetY = targetY;  // 设置目标Y坐标
		this.startingDuration = DUR;  // 设置持续时间
		this.duration = DUR;  // 初始化剩余时间为持续时间
		this.scale = Settings.scale;  // 设置缩放比例为当前游戏设置的缩放比例
		this.rotation = 0;  // 初始旋转角度为0
		this.color = color;  // 设置特效颜色
	}

	/**
	 * 播放随机的音效
	 * 根据随机结果播放不同的切割音效
	 */
	private void playRandomSfX() {
		int roll = MathUtils.random(5);  // 随机生成一个0到5之间的整数，决定播放哪个音效
		switch (roll) {
			case 0: {
				CardCrawlGame.sound.play("ATTACK_DAGGER_1");  // 播放音效1
				break;
			}
			case 1: {
				CardCrawlGame.sound.play("ATTACK_DAGGER_2");  // 播放音效2
				break;
			}
			case 2: {
				CardCrawlGame.sound.play("ATTACK_DAGGER_3");  // 播放音效3
				break;
			}
			case 3: {
				CardCrawlGame.sound.play("ATTACK_DAGGER_4");  // 播放音效4
				break;
			}
			case 4: {
				CardCrawlGame.sound.play("ATTACK_DAGGER_5");  // 播放音效5
				break;
			}
			default: {
				CardCrawlGame.sound.play("ATTACK_DAGGER_6");  // 播放音效6
			}
		}
	}

	/**
	 * 更新切割效果的状态
	 * - 更新位置（使用插值平滑过渡）
	 * - 更新透明度
	 * - 播放音效
	 */
	@Override
	public void update() {
		// 使用插值更新位置，使其从起始位置平滑过渡到目标位置
		this.x = Interpolation.fade.apply(this.targetX, this.startX, this.duration / this.startingDuration);
		this.y = Interpolation.fade.apply(this.targetY, this.startY, this.duration / this.startingDuration);

		if (!this.playedSound) {  // 如果音效未播放，则播放随机音效
			this.playRandomSfX();
			this.playedSound = true;
		}

		this.duration -= Gdx.graphics.getDeltaTime();  // 减少剩余的持续时间
		if (this.duration < 0.0f) {  // 如果效果完成，则设置isDone为true，标记为结束
			this.isDone = true;
		}

		// 更新透明度，使其在特效生命周期内渐变
		this.color.a = this.duration > 0.2f ?
				Interpolation.fade.apply(1.0f, 0.0f, (this.duration - 0.2f) * 5.0f) :
				Interpolation.fade.apply(0.0f, 1.0f, this.duration * 5.0f);
	}

	/**
	 * 渲染特效
	 * - 绘制特效图像，处理透明度和缩放
	 * @param sb SpriteBatch 用于批量绘制图像
	 */
	@Override
	public void render(SpriteBatch sb) {
		sb.setColor(this.color);  // 设置当前渲染颜色（包括透明度）

		// 绘制第一层特效图像
		sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth * 0.85f, (float)this.img.packedHeight / 2.0f,
				this.img.packedWidth, this.img.packedHeight, this.scale, this.scale * 1.5f, this.rotation);

		sb.setBlendFunction(770, 1);  // 设置混合模式为添加模式（加深效果）

		// 绘制第二层特效图像，缩放比例更小，形成层次感
		sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth * 0.85f, (float)this.img.packedHeight / 2.0f,
				this.img.packedWidth, this.img.packedHeight, this.scale * 0.75f, this.scale * 0.75f, this.rotation);

		sb.setBlendFunction(770, 771);  // 恢复默认混合模式
	}

	/**
	 * 清理资源
	 */
	@Override
	public void dispose() {
		// 当前没有使用额外的资源，因此无需清理
	}
}
