package com.aotter.trek.android.impression

import android.graphics.Rect
import android.view.View


/**
 * 2020/06/09 created by Anthony Wu
 * view可見度百分比計算者
 * 該類是針對view在螢幕上可見百分比的計算
 */
 object ViewVisibilityPercentageCalculator {

    /**
     * 計算可見百分比
     * 主要調用 view 的 getLocalVisibleRect 函數，取得 rect 去做計算
     */
    fun getVisibilityPercents(currentView: View): Int {

        var percents = 100
        val rect = Rect()
        //防止出現view已不在可見得範圍之内仍然返回100（完全可見）
        val isVisible = currentView.getLocalVisibleRect(rect)
        if (isVisible) {

            //可見時做百分比的計算
            val height = currentView.measuredHeight
            val width = currentView.measuredWidth

            //這邊用於計算view在垂直列表的百分比--//
            if (viewIsPartiallyHiddenTop(rect)) {
                percents = (height - rect.top) * 100 / height
            }
            if (viewIsPartiallyHiddenBottom(rect, height)) {
                percents = rect.bottom * 100 / height
            }
            //----------------------------------//

            //這邊用於計算view在水平列表的百分比--//
            if(viewIsPartiallyHiddenRight(rect,width)){
                percents = rect.right * 100 / width
            }
            if(viewIsPartiallyHiddenLeft(rect)){
                percents = (width - rect.left) * 100 / width
            }
            //-------------------------------//

        } else {
            //View已经不可見
            percents = 0
        }
        return percents
    }

    //view底部部分不可見
    private fun viewIsPartiallyHiddenBottom(
        rect: Rect,
        height: Int
    ): Boolean {
        return rect.bottom in 1 until height
    }

    //view頂部部分不可見
    private fun viewIsPartiallyHiddenTop(rect: Rect): Boolean {
        return rect.top > 0
    }


    //view右邊部分不可見
    private fun viewIsPartiallyHiddenRight(
        rect: Rect,
        width: Int
    ): Boolean {
        return rect.right in 1 until width
    }

    //view頂部部分不可見
    private fun viewIsPartiallyHiddenLeft(rect: Rect): Boolean {
        return rect.left > 0
    }



}