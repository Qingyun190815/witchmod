package witchmod.effects;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;  // 引入Gdx类，用于访问游戏时间等功能
import com.badlogic.gdx.graphics.Color;  // 引入颜色类，用于设置特效颜色
import com.badlogic.gdx.graphics.g2d.SpriteBatch;  // 引入SpriteBatch类，用于批量绘制图像
import com.badlogic.gdx.graphics.g2d.TextureAtlas;  // 引入TextureAtlas类，用于加载图像资源
import com.badlogic.gdx.math.Interpolation;  // 引入插值类，用于计算动画的过渡
import com.badlogic.gdx.math.MathUtils;  // 引入数学工具类，用于随机数和角度计算
import com.badlogic.gdx.math.Vector2;  // 引入Vector2类，用于表示2D坐标
import com.megacrit.cardcrawl.core.CardCrawlGame;  // 引入游戏核心类，用于播放声音等
import com.megacrit.cardcrawl.core.Settings;  // 引入设置类，用于获取屏幕缩放比例
import com.megacrit.cardcrawl.helpers.ImageMaster;  // 引入图像资源加载器
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;  // 引入抽象游戏特效类

/**
 * SkullFlaskEffect 类：模拟一个飞行瓶子（骷髅药水瓶）的效果。
 */
public class SkullFlaskEffect extends AbstractGameEffect {
	private static TextureAtlas.AtlasRegion img;  // 存储瓶子图像资源
	private float sX;  // 起始位置X
	private float sY;  // 起始位置Y
	private float cX;  // 当前X位置
	private float cY;  // 当前Y位置
	private float dX;  // 目标X位置
	private float dY;  // 目标Y位置
	private float yOffset;  // Y坐标偏移量，用于模拟瓶子的抖动
	private float bounceHeight;  // 向上的弹跳高度
	private static final float DUR = 0.6f;  // 持续时间
	private boolean playedSfx = false;  // 标记音效是否已经播放
	private ArrayList<Vector2> previousPos = new ArrayList<>();  // 用于记录瓶子的历史位置，创建尾迹效果

	/**
	 * 构造函数：初始化瓶子的起始位置、目标位置、弹跳高度等。
	 *
	 * @param srcX 起始X坐标
	 * @param srcY 起始Y坐标
	 * @param destX 目标X坐标
	 * @param destY 目标Y坐标
	 */
	public SkullFlaskEffect(float srcX, float srcY, float destX, float destY) {
		if (img == null) {
			img = ImageMaster.vfxAtlas.findRegion("combat/flask");  // 加载瓶子图像
		}
		this.sX = srcX;  // 设置起始X坐标
		this.sY = srcY;  // 设置起始Y坐标
		this.cX = this.sX;  // 当前X坐标初始化为起始X坐标
		this.cY = this.sY;  // 当前Y坐标初始化为起始Y坐标
		this.dX = destX;  // 设置目标X坐标
		this.dY = destY;  // 设置目标Y坐标
		this.rotation = 0.0f;  // 初始化旋转角度
		this.duration = DUR;  // 设置持续时间
		this.color = new Color(1.0f, 1.0f, 1.0f, 0.0f);  // 初始化透明度为0
		// 根据起始和目标位置计算瓶子的弹跳高度
		this.bounceHeight = this.sY > this.dY ? 400.0f * Settings.scale : this.dY - this.sY + 400.0f * Settings.scale;
	}

	/**
	 * 更新瓶子特效的位置、透明度、偏移量等属性。
	 */
	@Override
	public void update() {
		// 播放音效，确保音效只播放一次
		if (!this.playedSfx) {
			this.playedSfx = true;
			CardCrawlGame.sound.play("BLOOD_SPLAT", 0.3f);  // 播放溅血音效
		}
		// 使用线性插值更新当前坐标
		this.cX = Interpolation.linear.apply(this.dX, this.sX, this.duration / DUR);
		this.cY = Interpolation.linear.apply(this.dY, this.sY, this.duration / DUR);
		// 记录历史位置并生成尾迹效果
		this.previousPos.add(new Vector2(this.cX + MathUtils.random(-30.0f, 30.0f) * Settings.scale, this.cY + this.yOffset + MathUtils.random(-30.0f, 30.0f) * Settings.scale));
		if (this.previousPos.size() > 20) {
			this.previousPos.remove(0);  // 保持尾迹列表的最大长度为20
		}
		// 根据X轴方向更新旋转角度
		this.rotation = this.dX > this.sX ? (this.rotation -= Gdx.graphics.getDeltaTime() * 1000.0f) : (this.rotation += Gdx.graphics.getDeltaTime() * 1000.0f);
		// 更新透明度和偏移量的插值效果
		if (this.duration > 0.3f) {
			this.color.a = Interpolation.exp5In.apply(1.0f, 0.0f, (this.duration - 0.3f) / 0.3f) * Settings.scale;
			this.yOffset = Interpolation.circleIn.apply(this.bounceHeight, 0.0f, (this.duration - 0.3f) / 0.3f) * Settings.scale;
		} else {
			this.yOffset = Interpolation.circleOut.apply(0.0f, this.bounceHeight, this.duration / 0.3f) * Settings.scale;
		}
		this.duration -= Gdx.graphics.getDeltaTime();  // 减少持续时间
		if (this.duration < 0.0f) {
			this.isDone = true;  // 特效完成
		}
	}

	/**
	 * 渲染瓶子的图像及其尾迹。
	 *
	 * @param sb SpriteBatch 用于批量渲染图像
	 */
	@Override
	public void render(SpriteBatch sb) {
		sb.setBlendFunction(770, 1);  // 设置混合模式
		sb.setColor(new Color(1.0f, 1.0f, 1.0f, this.color.a / 3.0f));  // 设置尾迹的透明度
		// 绘制尾迹
		for (int i = 5; i < this.previousPos.size(); ++i) {
			sb.draw(ImageMaster.POWER_UP_2, this.previousPos.get(i).x - (float)(SkullFlaskEffect.img.packedWidth / 2), this.previousPos.get(i).y - (float)(SkullFlaskEffect.img.packedHeight / 2), (float)SkullFlaskEffect.img.packedWidth / 2.0f, (float)SkullFlaskEffect.img.packedHeight / 2.0f, SkullFlaskEffect.img.packedWidth, SkullFlaskEffect.img.packedHeight, this.scale / (40.0f / (float)i), this.scale / (40.0f / (float)i), this.rotation);
		}
		sb.setColor(this.color);  // 设置瓶子本身的颜色
		// 绘制瓶子
		sb.draw(img, this.cX - (float)(SkullFlaskEffect.img.packedWidth / 2), this.cY - (float)(SkullFlaskEffect.img.packedHeight / 2) + this.yOffset, (float)SkullFlaskEffect.img.packedWidth / 2.0f, (float)SkullFlaskEffect.img.packedHeight / 2.0f, SkullFlaskEffect.img.packedWidth, SkullFlaskEffect.img.packedHeight, this.scale, this.scale, this.rotation);
		sb.draw(img, this.cX - (float)(SkullFlaskEffect.img.packedWidth / 2), this.cY - (float)(SkullFlaskEffect.img.packedHeight / 2) + this.yOffset, (float)SkullFlaskEffect.img.packedWidth / 2.0f, (float)SkullFlaskEffect.img.packedHeight / 2.0f, SkullFlaskEffect.img.packedWidth, SkullFlaskEffect.img.packedHeight, this.scale, this.scale, this.rotation);
		sb.setBlendFunction(770, 771);  // 恢复混合模式
	}

	@Override
	public void dispose() {
		// 当前不需要清理资源
	}
}
