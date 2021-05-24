package com.aotter.trek.android.impression

import android.view.View
import kotlinx.coroutines.*

class ImpressionCountDownTimer : CoroutineScope by MainScope() {

    private var countDownJob: Job? = null

    private var impressionRequest: ImpressionRequest = ImpressionRequest()

    private var impressionListener: ImpressionListener? = null

    private var view: View? = null

    fun setImpressionRequest(impressionRequest: ImpressionRequest) {
        this.impressionRequest = impressionRequest
    }

    fun setImpressionListener(impressionListener: ImpressionListener?) {
        if (this.impressionListener != null) {
            stop()
            this.impressionListener = null
        }
        this.impressionListener = impressionListener
    }

    fun setView(view: View?) {
        this.view = view
    }

    fun checkPercent(percent: Int) {

        val startPercent = impressionRequest.getVisibleRangePercent()

        val millisInFuture = impressionRequest.getMillisInFuture()

        if (percent >= startPercent) {

            if (countDownJob == null) {

                countDownJob = launch(Dispatchers.Main) {

                    delay(millisInFuture)

                    view?.let {
                        impressionListener?.onImpressionSuccess(it)
                    }

                    stop()

                }

            }

        } else {

            stop()

        }
    }

    fun stop() {

        countDownJob?.let {
            it.cancel()
            countDownJob = null
        }

    }

}