package com.aotter.trek.android.impression

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

    private var impressionLifeCycleListener: ImpressionLifeCycleListener? = null

    private var impressionRequest =
            ImpressionRequest()

    private var impressionCountDownTimer = ImpressionCountDownTimer()

    init {

        lifecycle?.addObserver(this@ImpressionProvider)

        view.viewTreeObserver.addOnGlobalLayoutListener(this)

        view.addOnAttachStateChangeListener(this)

        view.viewTreeObserver.addOnScrollChangedListener(this)

        impressionCountDownTimer.setView(view)

    }

    fun impressionLifeCycleListener(@Nullable impressionLifeCycleListener: ImpressionLifeCycleListener): ImpressionProvider {

        this.impressionLifeCycleListener = impressionLifeCycleListener

        return this

    }

    fun impressionListener(@Nullable impressionListener: ImpressionListener): ImpressionProvider {
        if (this.impressionListener != null) {
            this.impressionListener = null
        }
        this.impressionListener = impressionListener

        return this
    }

    fun impressionRequest(@Nullable impressionRequest: ImpressionRequest): ImpressionProvider {

        this.impressionRequest = impressionRequest

        return this
    }


    fun apply() {

        impressionCountDownTimer.setImpressionRequest(this.impressionRequest)

        impressionCountDownTimer.setImpressionListener(impressionListener)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {


        impressionCountDownTimer.stop()

        impressionLifeCycleListener?.onLifeCyclePause()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {


        if (view.windowVisibility == View.VISIBLE || view.windowVisibility == View.INVISIBLE) {

            val percents = ViewVisibilityPercentageCalculator.getVisibilityPercents(view)


            impressionCountDownTimer.checkPercent(percents)

            if(percents == 0){
                impressionLifeCycleListener?.onLifeCyclePause()
                return
            }

            impressionLifeCycleListener?.onLifeCycleResume()

        }

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {


        lifecycle?.removeObserver(this@ImpressionProvider)

        view.viewTreeObserver.removeOnScrollChangedListener(this)

        view.removeOnAttachStateChangeListener(this)

        view.viewTreeObserver.removeOnGlobalLayoutListener(this)

        impressionLifeCycleListener?.onLifeCycleDestroy()

    }

    override fun onGlobalLayout() {

        //監聽view 是否狀態有變化 包刮了繪製完成

        resume()

        view.viewTreeObserver.removeOnGlobalLayoutListener(this)

    }

    override fun onViewAttachedToWindow(v: View?) {


        view.viewTreeObserver.addOnScrollChangedListener(this)

        lifecycle?.addObserver(this@ImpressionProvider)

    }

    override fun onViewDetachedFromWindow(v: View?) {


        view.viewTreeObserver.removeOnScrollChangedListener(this)

        lifecycle?.removeObserver(this@ImpressionProvider)

        impressionCountDownTimer.stop()

    }

    override fun onScrollChanged() {

        resume()

    }

}