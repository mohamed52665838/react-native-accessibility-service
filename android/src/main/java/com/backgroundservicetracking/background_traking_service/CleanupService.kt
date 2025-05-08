package com.backgroundservicetracking.background_traking_service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class CleanupService: Service() {
  private val TAG = "CleanupService"
  override fun onBind(intent: Intent?): IBinder? = null
  override fun onCreate() {
    super.onCreate()
    Log.d(TAG, "onCreate: Service Started")
  }
  override fun onTaskRemoved(rootIntent: Intent?) {
    super.onTaskRemoved(rootIntent)
    Log.d(TAG, "onTaskRemoved: Task Removed ")
    stopSelf()
  }
}
