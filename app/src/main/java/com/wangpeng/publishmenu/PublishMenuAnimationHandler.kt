package com.wangpeng.publishmenu

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import com.wangpeng.publishmenu.item.PublishMenuNormalItem
import com.wangpeng.publishmenu.item.PublishMenuSmallItem
import android.animation.PropertyValuesHolder
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.OvershootInterpolator


/**
 * Created by wangpeng on 2018/1/18.
 */
class PublishMenuAnimationHandler {
    private lateinit var publishMenu: PublishMenu

    fun setPublishMenu(publishMenu: PublishMenu) {
        this.publishMenu = publishMenu
    }

    fun smallInNormalOut() {
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
            animation.start()
            animation.addListener(object : CustomAnimatorListener() {
                override fun onAnimationEnd(animation: Animator?) {
                    if (small_index == publishMenu.smallItemList.size - 1) {
                        Log.i("smallInNormalOut", "smallIn ${small_index}")
                        for (normal_index in publishMenu.normalItemList.indices) {
                            Log.i("smallInNormalOut", "normal_index:" + normal_index)
                            var normalItem: PublishMenuNormalItem = publishMenu.normalItemList.get(normal_index)
                            var pvhX: PropertyValuesHolder? = null
                            var pvhY: PropertyValuesHolder? = null
                            if (publishMenu.openSmallOrNormal) {
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
                                    if (normal_index == publishMenu.normalItemList.size - 1) {
                                        Log.i("smallInNormalOut", "normalOut ${normal_index}")
                                        var mAlpha: AlphaAnimation = AlphaAnimation(0.0f, 1.0f)
                                        mAlpha.interpolator = AccelerateInterpolator()
                                        mAlpha.duration = 150
                                        normalItem.nameText?.animation = mAlpha
                                        normalItem.nameText?.startAnimation(mAlpha)
                                    }
                                }
                            })
                        }
                    }
                }
            })
        }
    }

    fun normalInSmallOut() {
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
            animation.interpolator = AccelerateInterpolator(1.5f)
            animation.start()
            animation.addListener(object : CustomAnimatorListener() {
                override fun onAnimationStart(animation: Animator?) {
                    var mAlpha: AlphaAnimation = AlphaAnimation(1.0f, 0.0f)
                    mAlpha.interpolator = AccelerateInterpolator()
                    mAlpha.duration = 150
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
                            if (publishMenu.openSmallOrNormal) {
                                pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, (smallItem.x - publishMenu?.mCenterPoint?.x!!).toFloat())
                                pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, (smallItem.y - publishMenu?.mCenterPoint?.y!!).toFloat())
                            } else {
                                pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0.0f)
                                pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f)
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
        }
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