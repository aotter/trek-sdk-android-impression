package com.aotter.trek.android.impression

import androidx.annotation.IntRange

/**
 * 2020/06/08 created by Anthony Wu
 * 曝光參數提供者
 * 該類提供開發者設置客製化的顯示百分比與停留時間
 * 可以針對 visibleRangePercent 與 dwellSeconds  預設為 1% 與 1秒
 * 兩個參數決定 view 要顯示多少百分比在屏幕上才開始計算曝光，曝光的成立可由
 * dwellSeconds參數決定計算時長
 */

class ImpressionRequest {


    private var visibleRangePercent = 50

    private var millisInFuture = 1000L

    /**
     * view顯示多少百分比後才算是在可見範圍
     * @param percent 該參數決定顯示該view多少百分比
     * 範圍限定是1％-100％
     */
    fun setVisibleRangePercent(@IntRange(from = 1, to = 100) percent: Int?): ImpressionRequest {

        if (percent != null && percent > 0) {
            visibleRangePercent = percent
        }

        return this
    }

    /**
     * view在可見範圍停留的秒數
     * @param millisInFuture 停留的秒數
     */
    fun millisInFuture(millisInFuture: Long?): ImpressionRequest {

        if (millisInFuture != null && millisInFuture > 1000L) {
            this.millisInFuture = millisInFuture
        }

        return this
    }

    fun getMillisInFuture() = millisInFuture

    fun getVisibleRangePercent() = visibleRangePercent


}