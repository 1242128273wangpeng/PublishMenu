package com.wangpeng.publishmenu.utils

import android.content.Context
import android.util.TypedValue
import android.util.TypedValue.COMPLEX_UNIT_DIP

/**
 * Created by wangpeng on 2018/1/16.
 */
class DensityUtil {
    companion object {
        fun getDisplayHeight(context: Context): Int {
            return context.resources.displayMetrics.heightPixels
        }

        fun getDisplayWidth(context: Context): Int {
            return context.resources.displayMetrics.widthPixels
        }

        fun dipToPixels(context: Context, dip: Float): Int {
            val r = context.resources
            val px = TypedValue.applyDimension(COMPLEX_UNIT_DIP, dip, r.displayMetrics)
            return px.toInt()
        }

        fun dip2px(var0: Context, var1: Float): Int {
            val var2 = var0.resources.displayMetrics.density
            return (var1 * var2 + 0.5f).toInt()
        }

        fun px2dip(var0: Context, var1: Float): Int {
            val var2 = var0.resources.displayMetrics.density
            return (var1 / var2 + 0.5f).toInt()
        }

        fun sp2px(var0: Context, var1: Float): Int {
            val var2 = var0.resources.displayMetrics.scaledDensity
            return (var1 * var2 + 0.5f).toInt()
        }

        fun px2sp(var0: Context, var1: Float): Int {
            val var2 = var0.resources.displayMetrics.scaledDensity
            return (var1 / var2 + 0.5f).toInt()
        }
    }
}