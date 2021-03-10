package com.aotter.trek.android.impression

import android.view.View


interface ImpressionLifeCycleListener {

    fun onLifeCycleResume()

    fun onLifeCyclePause()

    fun onLifeCycleDestroy()

}