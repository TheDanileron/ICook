package com.project.icook

import android.util.Log

class AppLogger {
    companion object{
        var isPrintLog = false

        fun i(tag: String, message:String) {
            if(isPrintLog)
                Log.i(tag, message)
        }
    }
}