package com.project.icook

object ConnectionHelper {
    var isNetworkAvailable = false
    val delayedTasksQueue = mutableListOf<() -> Unit>()
    val listeners = mutableListOf<OnNetworkAvailabilityListener>()

    // you can put tasks that require network connection here when no connection is provided
    // once the connection will be established the tasks will be executed
    fun executeDelayed() {
        delayedTasksQueue.forEach{
            it()
        }
    }

    fun onNetworkStateChanged(isNetworkAvailable: Boolean) {
        this.isNetworkAvailable = isNetworkAvailable
        if(isNetworkAvailable){
            executeDelayed()
            listeners.forEach{
                it.onAvailable()
            }
        } else {
            listeners.forEach{
                it.onLost()
            }
        }
    }

    fun subscribe(listener: OnNetworkAvailabilityListener) {
        if(!listeners.contains(listener))
            listeners.add(listener)
    }

    fun unsubscribe(listener: OnNetworkAvailabilityListener) {
        listeners.remove(listener)
    }
}