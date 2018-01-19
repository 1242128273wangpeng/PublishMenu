package com.wangpeng.publishmenu.waveview

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.wangpeng.publishmenu.utils.DensityUtil

/**
 * Created by wangpeng on 2018/1/16.
 */
class PublishWaveView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    //圆形背景画笔
    private lateinit var mBgPaint: Paint
    //圆形背景颜色
    private var mBgColor = 0x66FFFFFF
    //圆形背景的半径
    private var mBgRadius: Float = 30.0f
    //扩散水波的颜色
    private val mCircleEdgeColor_1 = 0xFF474C

    private val colors = intArrayOf(mCircleEdgeColor_1)
    //扩散水波的画笔
    private lateinit var mCircleWavePaint: Paint
    //扩散水波默认半径
    private var mDefaultWaveRadius: Float = 20.0f
    //当前扩散水波的半径
    private var mCurrentWaveRadius: Float = 0.0f
    //扩散水波执行时间
    private val mDuration: Long = 1800
    //水波动画
    private lateinit var fadeValueAnimators: ValueAnimator
    //动画集
    private var mAnimatorSet: AnimatorSet? = null

    private var mContext: Context

    init {
        mContext = context;
        mDefaultWaveRadius = DensityUtil.dip2px(context, 12.0f).toFloat()
        mBgRadius = DensityUtil.dip2px(context, 32.0f).toFloat()
        initBgPaint()
        initCirclePaint()
        for (i in 0 until colors.size) {
            initAnimation(i)
        }
        initAnimatorSet()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBg(canvas)
        drawWaveCircle(canvas)
    }

    /**
     * 画水波圆圈
     *
     * @param canvas 画布
     */
    private fun drawWaveCircle(canvas: Canvas) {
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mCurrentWaveRadius, mCircleWavePaint)
    }

    /**
     * 画圆形的背景
     *
     * @param canvas 画笔
     */
    private fun drawBg(canvas: Canvas) {
        mBgPaint.color = Color.TRANSPARENT
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), mBgRadius, mBgPaint)
    }

    /**
     * 开启动画
     */
    fun start() {
        if (!mAnimatorSet!!.isRunning) {
            mAnimatorSet?.start()
        }
    }

    /**
     * 暂停动画
     */
    fun pause() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAnimatorSet?.pause()
        }
    }

    /**
     * 继续播放动画
     */
    fun resume() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAnimatorSet?.resume()
        }
    }

    /**
     * 停止动画
     */
    fun stop() {
        mAnimatorSet?.end()
    }

    private fun initAnimatorSet() {
        mAnimatorSet = AnimatorSet()
        mAnimatorSet?.play(fadeValueAnimators)
    }

    private fun initAnimation(animation_i: Int) {
        // 同一个valueAnimators不能多次start,用无线播放的方式实现循环扩散动画
        fadeValueAnimators = ValueAnimator.ofFloat(0.0f, 1.0f)
        fadeValueAnimators.setDuration(mDuration)
        fadeValueAnimators.repeatCount = ValueAnimator.INFINITE
        fadeValueAnimators.repeatMode = ValueAnimator.RESTART
        fadeValueAnimators.interpolator = DecelerateInterpolator()!!
        fadeValueAnimators.addUpdateListener(ValueAnimator.AnimatorUpdateListener { valueAnimator ->
            val v = valueAnimator.animatedValue as Float
            mCurrentWaveRadius = mDefaultWaveRadius * v + mBgRadius
            mCircleWavePaint.color = colors[animation_i]
            mCircleWavePaint.alpha = ((1 - v) * 255).toInt()
            invalidate()
        })
    }

    private fun initCirclePaint() {
        mCircleWavePaint = Paint()
        mCircleWavePaint.strokeWidth = 2.0f
        mCircleWavePaint.setStyle(Paint.Style.STROKE)
        mCircleWavePaint.setAntiAlias(true)
        mCircleWavePaint.setColor(mCircleEdgeColor_1)
    }

    private fun initBgPaint() {
        mBgPaint = Paint()
        mBgPaint.setStyle(Paint.Style.FILL)
        mBgPaint.setAntiAlias(true)
        mBgPaint.setColor(mBgColor)
    }
}