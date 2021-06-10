package com.aotter.trek.android.impression

import android.view.View
import kotlinx.coroutines.*

class ImpressionCountDownTimer(private val view: View) : CoroutineScope by MainScope() {

    private var countDownJob: Job? = null

    private var impressionRequest: ImpressionRequest = ImpressionRequest()

    private var impressionListener: ImpressionListener? = null


    fun setImpressionRequest(impressionRequest: ImpressionRequest) {
        this.impressionRequest = impressionRequest
    }

    fun setImpressionListener(impressionListener: ImpressionListener?) {
        this.impressionListener = impressionListener
    }

    fun checkPercent(percent: Int) {

        val startPercent = impressionRequest.getVisibleRangePercent()

        val millisInFuture = impressionRequest.getMillisInFuture()

        if (percent >= startPercent) {

            if (countDownJob == null) {

                countDownJob = launch(Dispatchers.Main) {

                    delay(millisInFuture)

                    impressionListener?.onImpressionSuccess(view)

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