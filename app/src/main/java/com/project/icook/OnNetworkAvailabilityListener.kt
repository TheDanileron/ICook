package com.project.icook

interface OnNetworkAvailabilityListener {
    fun onAvailable()

    fun onLost()
}