package com.aryk.covid.helper

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

/**
 * ViewPager that handles wrap content by getting the max height of all child elements
 *
 * @author Abdul Khan, ar.yawarkhan@gmail.com
 * @since 21.07.2017
 *
 * see https://stackoverflow.com/a/20784791
 */
class WrapContentViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newWidthMeasureSpec = widthMeasureSpec
        var newHeightMeasureSpec = heightMeasureSpec

        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
            layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT
        ) {
            super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
        }

        if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            var height = 0

            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(
                    newWidthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
                )

                val measuredHeight = child.measuredHeight
                if (measuredHeight > height) {
                    height = measuredHeight
                }
            }

            newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }

        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            var width = 0

            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    newHeightMeasureSpec
                )

                val measuredWidth = child.measuredWidth
                if (measuredWidth > width) {
                    width = measuredWidth
                }
            }

            newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        }

        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
    }
}
