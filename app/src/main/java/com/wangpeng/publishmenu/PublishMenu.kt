package com.wangpeng.publishmenu

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.wangpeng.publishmenu.item.Item
import com.wangpeng.publishmenu.item.PublishMenuNormalItem
import com.wangpeng.publishmenu.item.PublishMenuSmallItem
import com.wangpeng.publishmenu.utils.DensityUtil

/**
 * Created by wangpeng on 2018/1/17.
 */
class PublishMenu @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    var normalItemList: MutableList<PublishMenuNormalItem> = mutableListOf()
    var smallItemList: MutableList<PublishMenuSmallItem> = mutableListOf()
    var paint: Paint? = null
    var mNormalRectF: RectF? = null
    var mSmallRectF: RectF? = null
    var mWidth: Float = 0.0f
    var mHeight: Float = 0.0f
    var mSmallPath: Path? = null
    var mNormalPath: Path? = null
    var mDrawFilter: PaintFlagsDrawFilter? = null
    var mNormalStartAngle: Float = 0.0f
    var mNormalEndAngle: Float = 0.0f
    var mSmallStartAngle: Float = 0.0f
    var mSmallEndAngle: Float = 0.0f
    var publishContiner: FrameLayout? = null
    var mHandler: Handler? = null
    var mCenterPoint: Point? = null
    var mSmallRadius: Float
    var mNormalRadius: Float
    var isFrameOrInnerPosition: Boolean = false
    var smallDrawables: IntArray = intArrayOf()
    var normalDrawables: IntArray = intArrayOf()
    var openSmallOrNormal: Boolean = false

    init {
        setWillNotDraw(false)
        mSmallPath = Path()
        mNormalPath = Path()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.color = Color.DKGRAY
        paint?.strokeWidth = 6.0f
        paint?.style = Paint.Style.STROKE
        mHandler = Handler()
        mSmallRadius = DensityUtil.dip2px(context, 60.0f).toFloat()
        mNormalRadius = DensityUtil.dip2px(context, 110.0f).toFloat()
        mDrawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    }

    fun init() {
        initItemList()
        initCenterPosition()
        initSmall()
        initNormal()
        mHandler?.postDelayed({
            initSmallWidgets(openSmallOrNormal)
            initNormalWidgets(!openSmallOrNormal)
        }, 60)
    }

    private fun initSmallWidgets(smallNeedShow: Boolean) {
        for (i in smallItemList.indices) {
            var smallItem: PublishMenuSmallItem = smallItemList.get(i)
            var frameLayout: FrameLayout.LayoutParams = FrameLayout.LayoutParams(smallItem.width, smallItem.height, Gravity.TOP or Gravity.LEFT)
            if (smallNeedShow) {
                frameLayout.setMargins(smallItem.x - smallItem.width / 2, smallItem.y - smallItem.height / 2, 0, 0)
                smallItem.alpha = 1.0f
                smallItem?.iconImg?.alpha = 1.0f
            } else {
                frameLayout.setMargins((mWidth.toInt() - smallItem.width) / 2, (mHeight.toInt() - smallItem.height) / 2, 0, 0)
            }
            if (smallItem?.iconImg?.parent != null) {
                var viewGroup: ViewGroup = smallItem?.iconImg?.parent as ViewGroup
                viewGroup?.removeView(smallItem.iconImg)
            }
            addViewToCurrentContainer(smallItem?.iconImg as View, frameLayout)

        }
    }

    private fun initNormalWidgets(normalNeedShow: Boolean) {
        for (i in normalItemList.indices) {
            var normalItem: PublishMenuNormalItem = normalItemList.get(i)
            var frameLayout: FrameLayout.LayoutParams = FrameLayout.LayoutParams(normalItem.width, normalItem.height, Gravity.TOP or Gravity.LEFT)
            if (normalNeedShow) {
                frameLayout.setMargins(normalItem.x - normalItem.width / 2, normalItem.y - normalItem.height / 2, 0, 0)
                normalItem.alpha = 1.0f
                normalItem?.iconImg?.alpha = 1.0f
            } else {
                frameLayout.setMargins((mWidth.toInt() - normalItem.width) / 2, (mHeight.toInt() - normalItem.height) / 2, 0, 0)
            }
            if (normalItem?.iconImg?.parent != null) {
                var viewGroup: ViewGroup = normalItem?.iconImg?.parent as ViewGroup
                viewGroup?.removeView(normalItem.iconImg)
            }
            addViewToCurrentContainer(normalItem?.iconImg as View, frameLayout)
        }
    }

    private fun initCenterPosition() {
        // 为了调试画弧线
        mCenterPoint = calculateItemPositions(isFrameOrInnerPosition)
        mNormalRectF = RectF((mWidth / 2 - mNormalRadius), (mHeight / 2 - mNormalRadius), (mWidth / 2 + mNormalRadius), (mHeight / 2 + mNormalRadius))
        mSmallRectF = RectF((mWidth / 2 - mSmallRadius), (mHeight / 2 - mSmallRadius), (mWidth / 2 + mSmallRadius), (mHeight / 2 + mSmallRadius))
        mNormalPath?.addArc(mNormalRectF, mNormalStartAngle, Math.abs((mNormalEndAngle - mNormalStartAngle)))
        mSmallPath?.addArc(mSmallRectF, mSmallStartAngle, Math.abs((mSmallEndAngle - mSmallStartAngle)))
        postInvalidate()
    }

    private fun initItemList() {
        for (normal_i in 0 until 6) {
            var item: PublishMenuNormalItem = PublishMenuNormalItem(0, 0)
            item.iconImg = ImageView(context)
            item.iconImg?.alpha = 0.0f
            item.iconImg?.layoutParams = FrameLayout.LayoutParams(DensityUtil.dip2px(context, 75.0f), DensityUtil.dip2px(context, 75.0f))
            item.iconImg?.setImageResource(normalDrawables!![normal_i])
            item.nameText = TextView(context)
            item.nameText?.alpha = 0.0f
            item.nameText?.text = "啊啊啊"
            normalItemList.add(item)
        }
        for (small_i in 0 until 6) {
            var item: PublishMenuSmallItem = PublishMenuSmallItem(0, 0)
            item.iconImg = ImageView(context)
            item.iconImg?.alpha = 0.0f
            item.iconImg?.layoutParams = FrameLayout.LayoutParams(DensityUtil.dip2px(context, 20.0f), DensityUtil.dip2px(context, 20.0f))
            item.iconImg?.setImageResource(smallDrawables!![small_i])
            smallItemList.add(item)
        }
    }

    /**
     * runnable持续的计算item的大小，直到测量出结果
     */
    private inner class ItemViewQueueListener(private val item: Item) : Runnable {
        private val tries: Int
        private val MAX_TRIES = 20

        init {
            this.tries = 0
        }

        override fun run() {
            Log.i("run", "running...${item is PublishMenuSmallItem} or ${item is PublishMenuNormalItem}")
            if (item.iconImg?.getMeasuredWidth() == 0 && tries < MAX_TRIES) {
                item.iconImg?.post(this)
                Log.i("run", "==0 tries:" + tries)
                return
            }
            item.width = item.iconImg?.getMeasuredWidth()!!
            item.height = item.iconImg?.getMeasuredHeight()!!
            Log.i("run", "tries:" + tries + " width:${item.width}  height:${item.height}")
            item.iconImg?.setAlpha(item.alpha)
            removeViewFromCurrentContainer(item.iconImg!!)
            if (item is PublishMenuNormalItem) {
                var normalItem: PublishMenuNormalItem = item as PublishMenuNormalItem
                normalItem?.nameText?.alpha = item.alpha
                removeViewFromCurrentContainer(normalItem.nameText)
            }
        }
    }

    fun addViewToCurrentContainer(view: View?) {
        if (view != null) {
            removeViewFromCurrentContainer(view)
            publishContiner?.addView(view)
        }
    }

    fun addViewToCurrentContainer(view: View, layoutParams: FrameLayout.LayoutParams) {
        removeViewFromCurrentContainer(view)
        if (layoutParams != null) {
            publishContiner?.addView(view, layoutParams)
        } else {
            publishContiner?.addView(view)
        }
    }

    fun removeViewFromCurrentContainer(view: View?) {
        publishContiner?.removeView(view)
    }

    private fun initNormal() {
        for (i in 0 until normalItemList.size) {
            var normalItem: PublishMenuNormalItem = normalItemList.get(i);
            addViewToCurrentContainer(normalItem.iconImg)
            addViewToCurrentContainer(normalItem.nameText)
            if (normalItem.width == 0 || normalItem.height == 0) {
                if (normalItem.iconImg?.isAttachedToWindow!!) {
                    normalItem.iconImg?.post(ItemViewQueueListener(normalItem))
                } else {
                    Log.i("init", "initNormal not attach")
                }
            }
        }
        // 大圆的位置
        val normalArea: RectF = RectF((mCenterPoint?.x!! - mNormalRadius).toFloat(), (mCenterPoint?.y!! - mNormalRadius).toFloat(), (mCenterPoint?.x!! + mNormalRadius).toFloat(), (mCenterPoint?.y!! + mNormalRadius).toFloat())
        var normalOrbit: Path = Path()
        normalOrbit.addArc(normalArea, mNormalStartAngle.toFloat(), Math.abs((mNormalEndAngle - mNormalStartAngle)).toFloat())
        val normalMeasure: PathMeasure = PathMeasure(normalOrbit, false)
        // 边测量边标记外层点的位置
        for (i in normalItemList.indices) {
            val coords = floatArrayOf(0f, 0f)
            normalMeasure.getPosTan(i * normalMeasure.getLength() / (normalItemList.size - 1), coords, null)
            normalItemList.get(i).x = coords[0].toInt() - normalItemList.get(i).width / 2
            normalItemList.get(i).y = coords[1].toInt() - normalItemList.get(i).height / 2
            Log.i("normalItemList", "i:" + i + " x:" + normalItemList.get(i).x + " y:" + normalItemList.get(i).y)
        }
    }

    private fun initSmall() {
        for (i in 0 until smallItemList.size) {
            var smallItem: PublishMenuSmallItem = smallItemList.get(i);
            addViewToCurrentContainer(smallItem.iconImg)
            if (smallItem.width == 0 || smallItem.height == 0) {
                if (smallItem.iconImg?.isAttachedToWindow!!) {
                    smallItem.iconImg?.post(ItemViewQueueListener(smallItem))
                } else {
                    Log.i("init", "initSmall not attach")
                }
            }
        }
        // 小圆的位置
        val smallArea: RectF = RectF((mCenterPoint?.x!! - mSmallRadius).toFloat(), (mCenterPoint?.y!! - mSmallRadius).toFloat(), (mCenterPoint?.x!! + mSmallRadius).toFloat(), (mCenterPoint?.y!! + mSmallRadius).toFloat())
        var orbit: Path = Path()
        orbit.addArc(smallArea, mSmallStartAngle.toFloat(), Math.abs((mSmallEndAngle - mSmallStartAngle)).toFloat())
        val smallMeasure: PathMeasure = PathMeasure(orbit, false)
        // 边测量边标记内层点的位置
        for (i in smallItemList.indices) {
            val coords = floatArrayOf(0f, 0f)
            smallMeasure.getPosTan(i * smallMeasure.getLength() / (smallItemList.size - 1), coords, null)
            smallItemList.get(i).x = coords[0].toInt() - smallItemList.get(i).width / 2
            smallItemList.get(i).y = coords[1].toInt() - smallItemList.get(i).height / 2
            Log.i("smallItemList", "i:" + i + " x:" + smallItemList.get(i).x + " y:" + smallItemList.get(i).y)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawFilter = mDrawFilter
        if (mSmallPath != null) {
            canvas?.drawPath(mSmallPath, paint)
        } else {
            Log.i("onDraw", "mSmallPath==null")
        }
        if (mNormalPath != null) {
            canvas?.drawPath(mNormalPath, paint)
        } else {
            Log.i("onDraw", "mNormalPath==null")
        }
        Log.i("onDraw", "normalItemList size:" + normalItemList.size)
        Log.i("onDraw", "smallItemList size:" + smallItemList.size)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.i("onLayout", "onLayout width:" + measuredWidth + "onLayout height:" + measuredHeight)
        mWidth = measuredWidth.toFloat()
        mHeight = measuredHeight.toFloat()
    }

    fun calculateItemPositions(isFramePositionBoolean: Boolean): Point {
        var x: Int = 0
        var y: Int = 0
        if (isFramePositionBoolean) {
            val coords = IntArray(2)
            this.getLocationOnScreen(coords)
            Log.i("calculateItemPositions", "coords[0]:" + coords[0] + "  coords[1]:" + coords[1])
            Log.i("calculateItemPositions", "measuredWidth:" + this.measuredWidth + "  measuredHeight:" + this.measuredHeight)
            x = coords[0] + measuredWidth / 2
            y = coords[1] + measuredHeight / 2
        } else {
            x = measuredWidth / 2
            y = measuredHeight / 2
        }
        Log.i("calculateItemPositions", "x:" + x + "  y:" + y)
        return Point(x, y)
    }

}