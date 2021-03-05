package com.aotter.trek.android.impression

import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.Nullable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


/**
 * 2020/06/08 created by Anthony Wu
 * 曝光提供者
 * 這裡會接收到 view 與 lifecycle
 * 會依照職責配發任務下去給其他類，如下
 * ImpressionListener 負責曝光的回調監聽
 * ViewVisibilityPercentageCalculator 負責計算可見百分比
 * ImpressionRequest 客製化的百分比參數與停留時間
 * ImpressionCountDownTimer 曝光計時器
 */
class ImpressionProvider(private val view: View, private val lifecycle: Lifecycle? = null) :
    LifecycleObserver, ViewTreeObserver.OnGlobalLayoutListener,
    View.OnAttachStateChangeListener, ViewTreeObserver.OnScrollChangedListener {

    private var impressionListener: ImpressionListener? = null

    private var impressionRequest =
        ImpressionRequest()

    init {

        view.viewTreeObserver.addOnGlobalLayoutListener(this)

        view.addOnAttachStateChangeListener(this)

        view.viewTreeObserver.addOnScrollChangedListener(this)

        ImpressionCountDownTimer.setView(view)

    }

    fun impressionListener(@Nullable impressionListener: ImpressionListener): ImpressionProvider {

        this.impressionListener = impressionListener

        return this
    }

    fun impressionRequest(@Nullable impressionRequest: ImpressionRequest): ImpressionProvider {

        this.impressionRequest = impressionRequest

        return this
    }


    fun apply() {

        ImpressionCountDownTimer.setImpressionRequest(this.impressionRequest)

        ImpressionCountDownTimer.setImpressionListener(impressionListener)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {

        Log.e("Lifecycle", "pause")

        ImpressionCountDownTimer.stop()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {

        Log.e("Lifecycle", "resume")

        if(view.windowVisibility == View.VISIBLE){
            val percents = ViewVisibilityPercentageCalculator.getVisibilityPercents(view)

            Log.e("Lifecycle", percents.toString())

            ImpressionCountDownTimer.checkPercent(percents)
        }

    }

    override fun onGlobalLayout() {

        //監聽view 是否狀態有變化 包刮了繪製完成

        resume()

        view.viewTreeObserver.removeOnGlobalLayoutListener(this)

    }

    override fun onViewAttachedToWindow(v: View?) {

        Log.e("view", "view 出現在螢幕上")

        view.viewTreeObserver.addOnScrollChangedListener(this)

        lifecycle?.addObserver(this@ImpressionProvider)

    }

    override fun onViewDetachedFromWindow(v: View?) {

        Log.e("view", "view 消失在螢幕上")

        view.viewTreeObserver.removeOnScrollChangedListener(this)

        lifecycle?.removeObserver(this@ImpressionProvider)

        ImpressionCountDownTimer.stop()

    }

    override fun onScrollChanged() {

        resume()

    }

}