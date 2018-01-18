package com.wangpeng.publishmenu

import android.view.View
import android.view.animation.*

/**
 * Created by wangpeng on 2018/1/17.
 */
class PublishMenuHelper {
    companion object {
        var isRunning: Boolean = false

        /**
         * @param view 执行动画旋转的控件
         * @param cw 正向(true) 反向(false)
         */
        fun rorateCenter(view: View, cw: Boolean) {
            var rotateAnimation: RotateAnimation? = null
            if (cw) {
                rotateAnimation = RotateAnimation(0.0f, 315.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            } else {
                rotateAnimation = RotateAnimation(0.0f, -270.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            }
            rotateAnimation.fillAfter = true
            rotateAnimation.interpolator = DecelerateInterpolator()
            rotateAnimation.duration = 300
            if (!isRunning) {
                view.startAnimation(rotateAnimation)
            }
        }

        /**
         * @param view 执行动画缩放的控件
         * @param smallOrBig 变小(true) 变大(false)
         */
        fun scaleCenter(view: View, smallOrBig: Boolean, mAnimationListener: AnimationListener) {
            var scaleAnimation: ScaleAnimation? = null
            if (smallOrBig) {
                scaleAnimation = ScaleAnimation(1.0f, 0.6f, 1.0f, 0.6f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            } else {
                scaleAnimation = ScaleAnimation(0.6f, 1f, 0.6f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            }
            scaleAnimation.fillAfter = true
            scaleAnimation.interpolator = DecelerateInterpolator()
            scaleAnimation.duration = 300
            scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    mAnimationListener.onAnimationEnd(animation)
                    isRunning = false
                }

                override fun onAnimationStart(animation: Animation?) {
                    isRunning = true
                    mAnimationListener.onAnimationStart(animation)
                }
            })
            if (!isRunning) {
                view.startAnimation(scaleAnimation)
            }
        }

        fun setNormalStartEndAngle(publishMenu: PublishMenu, start: Float, end: Float) {
            publishMenu.mNormalStartAngle = start
            publishMenu.mNormalEndAngle = end
        }

        fun setSmallStartEndAngle(publishMenu: PublishMenu, start: Float, end: Float) {
            publishMenu.mSmallStartAngle = start
            publishMenu.mSmallEndAngle = end
        }

    }

    interface AnimationListener {
        fun onAnimationStart(animation: Animation?);

        fun onAnimationEnd(animation: Animation?)
    }
}