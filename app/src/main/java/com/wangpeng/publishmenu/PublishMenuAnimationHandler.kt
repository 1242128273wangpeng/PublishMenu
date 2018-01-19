package com.wangpeng.publishmenu

import android.animation.*
import com.wangpeng.publishmenu.item.PublishMenuNormalItem
import com.wangpeng.publishmenu.item.PublishMenuSmallItem
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationUtils
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import de.hdodenhof.circleimageview.CircleImageView


/**
 * Created by wangpeng on 2018/1/18.
 */
class PublishMenuAnimationHandler {
    var mIsOpen: Boolean = false
    private lateinit var publishMenu: PublishMenu

    fun setPublishMenu(publishMenu: PublishMenu) {
        this.publishMenu = publishMenu
        this.mIsOpen = publishMenu.openSmallOrNormal
    }

    fun smallInNormalOut() {
        var bakIcon: ImageView? = getBakIcon()
        for (small_index in publishMenu.smallItemList.indices) {
            Log.i("smallInNormalOut", "small_index:" + small_index)
            var smallItem: PublishMenuSmallItem = publishMenu.smallItemList.get(small_index)
            var flaot_x: Float = -(smallItem.x - publishMenu?.mCenterPoint?.x!!).toFloat()
            var float_y: Float = -(smallItem.y - publishMenu?.mCenterPoint?.y!!).toFloat()
            val pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, flaot_x)
            val pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, float_y)
            Log.i("smallInNormalOut", "  smallItem.x:" + smallItem.x + "   smallItem.y" + smallItem.y)
            val pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, -720.0f)
            val pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.0f)
            val pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.0f)
            val pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f)
            val animation = ObjectAnimator.ofPropertyValuesHolder(smallItem.iconImg, pvhX, pvhY, pvhR, pvhsX, pvhsY, pvhA)
            animation.duration = 300
            animation.interpolator = AccelerateInterpolator(1.5f)
            animation.addListener(object : CustomAnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    if (small_index == publishMenu.smallItemList.size - 1) {
                        Log.i("smallInNormalOut", "smallIn ${small_index}")
                        for (normal_index in publishMenu.normalItemList.indices) {
                            Log.i("smallInNormalOut", "normal_index:" + normal_index)
                            var normalItem: PublishMenuNormalItem = publishMenu.normalItemList.get(normal_index)
                            var pvhX: PropertyValuesHolder? = null
                            var pvhY: PropertyValuesHolder? = null
                            // 默认normal打开 -----2
                            if (mIsOpen) {
                                pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, (normalItem.x - publishMenu?.mCenterPoint?.x!!).toFloat())
                                pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, (normalItem.y - publishMenu?.mCenterPoint?.y!!).toFloat())
                            } else {
                                pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0.0f)
                                pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f)
                            }
                            Log.i("smallInNormalOut", "  publishMenu?.mCenterPoint?.x:" + publishMenu?.mCenterPoint?.x + "   publishMenu?.mCenterPoint?.y" + publishMenu?.mCenterPoint?.y)
                            val pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 720.0f)
                            val pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f)
                            val pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f)
                            val pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f)
                            val animation = ObjectAnimator.ofPropertyValuesHolder(normalItem.iconImg, pvhX, pvhY, pvhR, pvhsX, pvhsY, pvhA)
                            animation.duration = 300
                            animation.interpolator = OvershootInterpolator(1.5f)
                            animation.start()
                            animation.addListener(object : CustomAnimatorListener() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    var mAlpha: AlphaAnimation = AlphaAnimation(0.0f, 1.0f)
                                    mAlpha.interpolator = AccelerateInterpolator()
                                    mAlpha.duration = 300
                                    mAlpha.fillAfter = true
                                    normalItem.nameText?.alpha = 1.0f
                                    normalItem.nameText?.animation = mAlpha
                                    normalItem.nameText?.startAnimation(mAlpha)
                                    if (normal_index == publishMenu.normalItemList.size - 1) {
                                        Log.i("smallInNormalOut", "normalOut ${normal_index}")
                                        publishMenu.publishContiner?.postDelayed({
                                            turnIcon(publishMenu.context, false,object :TurnEndListener{
                                                override fun onTurnEnd() {

                                                }
                                            })
                                        }, 2000)
                                    }
                                }
                            })
                            if (normal_index == 3 && bakIcon != null) {
                                var bakpvhX: PropertyValuesHolder? = null
                                var bakpvhY: PropertyValuesHolder? = null
                                if (mIsOpen) {
                                    bakpvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, (normalItem.x - publishMenu?.mCenterPoint?.x!!).toFloat())
                                    bakpvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, (normalItem.y - publishMenu?.mCenterPoint?.y!!).toFloat())
                                } else {
                                    bakpvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0.0f)
                                    bakpvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f)
                                }
                                val bakpvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 720.0f)
                                val bakpvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f)
                                val bakpvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f)
                                val bakpvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f)
                                var bakanimation = ObjectAnimator.ofPropertyValuesHolder(bakIcon, bakpvhX, bakpvhY, bakpvhR, bakpvhsX, bakpvhsY, bakpvhA)
                                bakanimation.duration = 300
                                bakanimation.interpolator = OvershootInterpolator(1.5f)
                                Log.i("smallInNormalOut", "bakIcon x: ${bakIcon?.x} y: ${bakIcon?.y}")
                                bakanimation.start()
                            }
                        }
                    }
                }
            })
            animation.start()
        }
    }

    fun normalInSmallOut() {
        var bakIcon: ImageView? = getBakIcon()
        for (normal_index in publishMenu.normalItemList.indices) {
            Log.i("normalInSmallOut", "normal_index:" + normal_index)
            var normalItem: PublishMenuNormalItem = publishMenu.normalItemList.get(normal_index)
            val pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -(normalItem.x - publishMenu?.mCenterPoint?.x!!).toFloat())
            val pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -(normalItem.y - publishMenu?.mCenterPoint?.y!!).toFloat())
            val pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, -720.0f)
            val pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.0f)
            val pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.0f)
            val pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f)
            val animation = ObjectAnimator.ofPropertyValuesHolder(normalItem.iconImg, pvhX, pvhY, pvhR, pvhsX, pvhsY, pvhA)
            animation.duration = 300
            animation.interpolator = AccelerateInterpolator(1.5f)!!
            animation.addListener(object : CustomAnimatorListener() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    var mAlpha: AlphaAnimation = AlphaAnimation(1.0f, 0.0f)
                    mAlpha.interpolator = AccelerateInterpolator()
                    mAlpha.duration = 300
                    mAlpha.fillAfter = true
                    normalItem.nameText?.animation = mAlpha
                    normalItem.nameText?.startAnimation(mAlpha)
                }

                override fun onAnimationEnd(animation: Animator?) {
                    if (normal_index == publishMenu.normalItemList.size - 1) {
                        Log.i("normalInSmallOut", "normalOut ${normal_index}")
                        for (small_index in publishMenu.smallItemList.indices) {
                            Log.i("normalInSmallOut", "small_index:" + small_index)
                            var smallItem: PublishMenuSmallItem = publishMenu.smallItemList.get(small_index)
                            var pvhX: PropertyValuesHolder? = null
                            var pvhY: PropertyValuesHolder? = null
                            // 默认small打开 -----1
                            if (mIsOpen) {
                                pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0.0f)
                                pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f)
                            } else {
                                pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, (smallItem.x - publishMenu?.mCenterPoint?.x!!).toFloat())
                                pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, (smallItem.y - publishMenu?.mCenterPoint?.y!!).toFloat())
                            }
                            val pvhR = PropertyValuesHolder.ofFloat(View.ROTATION, 720.0f)
                            val pvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f)
                            val pvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f)
                            val pvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f)
                            val animation = ObjectAnimator.ofPropertyValuesHolder(smallItem.iconImg, pvhX, pvhY, pvhR, pvhsX, pvhsY, pvhA)
                            animation.duration = 300
                            animation.interpolator = OvershootInterpolator(1.5f)
                            animation.start()
                            animation.addListener(object : CustomAnimatorListener() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    if (small_index == publishMenu.smallItemList.size - 1) {
                                        Log.i("normalInSmallOut", "smallIn ${small_index}")
                                    }
                                }
                            })
                        }
                    }
                }
            })
            publishMenu.publishContiner?.postDelayed({
                turnIcon(publishMenu.context, false,object :TurnEndListener{
                    override fun onTurnEnd() {
                        animation.start()
                        if (normal_index == 3 && bakIcon != null) {
                            var bakpvhX: PropertyValuesHolder? = null
                            var bakpvhY: PropertyValuesHolder? = null
                            if (mIsOpen) {
                                bakpvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0.0f)
                                bakpvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f)
                            } else {
                                bakpvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -(bakIcon.x - publishMenu?.mCenterPoint?.x!!).toFloat())
                                bakpvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -(bakIcon.y - publishMenu?.mCenterPoint?.y!!).toFloat())
                            }
                            val bakpvhR = PropertyValuesHolder.ofFloat(View.ROTATION, -720.0f)
                            val bakpvhsX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.0f)
                            val bakpvhsY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.0f)
                            val bakpvhA = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f)
                            var bakanimation = ObjectAnimator.ofPropertyValuesHolder(bakIcon, bakpvhX, bakpvhY, bakpvhR, bakpvhsX, bakpvhsY, bakpvhA)
                            bakanimation.duration = 300
                            bakanimation.interpolator = OvershootInterpolator(1.5f)
                            Log.i("smallInNormalOut", "bakIcon x: ${bakIcon?.x} y: ${bakIcon?.y}")
                            bakanimation.start()
                        }
                    }
                })
            }, 2000)
        }
    }

    // 改变视角距离, 贴近屏幕
    fun setCameraDistance(context: Context, mFlCardFront_: ImageView?, mFlCardBack_: ImageView?) {
        val distance = 16000
        val scale = context.resources.displayMetrics.density * distance
        mFlCardFront_?.cameraDistance = scale
        mFlCardBack_?.cameraDistance = scale
    }

    fun turnIcon(context: Context, mIsShowBack: Boolean, mTurnEndListener: TurnEndListener?) {
        var mLeftInSet: Animator = AnimatorInflater.loadAnimator(context, R.animator.anim_in) as AnimatorSet
        var mRightOutSet: Animator = AnimatorInflater.loadAnimator(context, R.animator.anim_out) as AnimatorSet
        var bakIcon: CircleImageView? = getBakIcon()
        var realsIcon: ImageView? = getVideoIcon()
        bakIcon?.isBorderOverlay = true
        bakIcon?.borderWidth = 6
        bakIcon?.borderColor = context.resources.getColor(R.color.bordercolor)
        bakIcon?.scaleType = ImageView.ScaleType.CENTER_CROP
        setCameraDistance(context, realsIcon, bakIcon)
        var isShowBack: Boolean = mIsShowBack
        // 正面朝上
        if (!isShowBack) {
            mRightOutSet.setTarget(realsIcon)
            mLeftInSet.setTarget(bakIcon)
            mRightOutSet.start()
            mLeftInSet.start()
        } else { // 背面朝上
            mRightOutSet.setTarget(bakIcon)
            mLeftInSet.setTarget(realsIcon)
            mRightOutSet.start()
            mLeftInSet.start()
        }
        mLeftInSet.addListener(object : CustomAnimatorListener() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                mTurnEndListener?.onTurnEnd()
            }
        })
        isShowBack = !isShowBack
    }

    private fun getVideoIcon(): ImageView? {
        for (i in publishMenu.normalItemList.indices) {
            if (i == 3) {
                return publishMenu.normalItemList.get(i).iconImg
            }
        }
        return null
    }

    private fun getBakIcon(): CircleImageView? {
        for (child_index in 0 until publishMenu.publishContiner?.childCount!!) {
            var view: View = publishMenu?.publishContiner?.getChildAt(child_index)!!
            if (-10 == view.getTag()) {
                var bakIcon = view as CircleImageView
                return bakIcon
            }
        }
        return null
    }

    interface TurnEndListener {
        fun onTurnEnd()
    }

    abstract class CustomAnimatorListener : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
        }
    }
}