package com.aotter.trek.android.impression

import android.os.CountDownTimer
import android.util.Log
import android.view.View


object ImpressionCountDownTimer {

    private var countDownTimer: CountDownTimer? = null

    private  var impressionRequest: ImpressionRequest = ImpressionRequest()

    private var impressionListener: ImpressionListener? = null

    private var view: View? = null

    private const val COUNT_DOWN_INTERVAL = 1000L


    fun setImpressionRequest(impressionRequest: ImpressionRequest) {
        ImpressionCountDownTimer.impressionRequest = impressionRequest
    }

    fun setImpressionListener(impressionListener: ImpressionListener?) {
        ImpressionCountDownTimer.impressionListener = impressionListener
    }

    fun setView(view: View?) {
        ImpressionCountDownTimer.view = view
    }

    fun checkPercent(percent: Int) {

        val startPercent = impressionRequest.getVisibleRangePercent()

        val millisInFuture = impressionRequest.getMillisInFuture()

        if (percent >= startPercent) {

            if (countDownTimer == null) {

                countDownTimer = object : CountDownTimer(
                    millisInFuture,
                    COUNT_DOWN_INTERVAL
                ) {
                    override fun onFinish() {

                        stop()

                        view?.let {
                            impressionListener?.onImpressionSuccess(it)
                        }

                    }

                    override fun onTick(millisUntilFinished: Long) {

                        Log.e("onTick", millisUntilFinished.toString())

                    }
                }

                countDownTimer?.start()

            }


        } else {

            stop()

        }

    }

    fun stop() {

        countDownTimer?.let {

            it.cancel()

            countDownTimer = null

        }

    }


}