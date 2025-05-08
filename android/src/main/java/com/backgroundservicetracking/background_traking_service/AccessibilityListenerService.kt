package com.backgroundservicetracking.background_traking_service

import android.accessibilityservice.AccessibilityService
import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import java.time.Clock
import java.time.LocalTime
import java.util.Date
import androidx.core.net.toUri
import com.backgroundservicetracking.R
import java.util.Timer
import java.util.TimerTask

class AccessibilityListenerService: AccessibilityService() {
  private val TAG = "AccessibilityListenerSe"
  private val NOTIFICATION_ID_SWITCH_APP = "super_drive_safe_notification_channel_switch_app"
  private val NOTIFICATION_ID_TEXGING = "super_drive_safe_notification_channel_texting"
  private val NOTIFICATION_ID_SCROLLING = "super_drive_safe_notification_channel_scrolling"
  private val whiteListPackagePatterns = listOf("com.android", "home", "com.google.android.apps.maps")
  private var lastTimeNotificationSent = 0L
  val time = Timer()


  override fun onCreate() {
    super.onCreate()
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val soundResId = resources.getIdentifier("notification_sound", "raw", packageName)
    if (soundResId == 0) {
      Log.w("Notification", "Sound resource not found")
      return
    }

    val soundUriSwitchApp = "android.resource://${applicationContext.packageName}/raw/beaware_switch_app".toUri()
    val soundUriTexting = "android.resource://${applicationContext.packageName}/raw/beaware_texting_detected".toUri()
    val soundUriScrolling = "android.resource://${applicationContext.packageName}/raw/scrolling_detected".toUri()

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      try {

        notificationManager.createNotificationChannel(
          NotificationChannel(NOTIFICATION_ID_SWITCH_APP, "Super Drive Safe, Switching", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Super Drive Safe Notification channel"
            setSound(soundUriSwitchApp, AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_ALARM)
              .build())
          }
        )

        notificationManager.createNotificationChannel(
          NotificationChannel(NOTIFICATION_ID_TEXGING, "Super Drive Safe Texting", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Super Drive Safe Notification channel"
            setSound(soundUriTexting, AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_ALARM)
              .build())
          }
        )

        notificationManager.createNotificationChannel(
          NotificationChannel(NOTIFICATION_ID_SCROLLING, "Super Drive Safe Scrolling", NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Super Drive Safe Notification channel"
            setSound(soundUriScrolling, AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_ALARM)
              .build())
          }
        )
      } catch (e: Exception) {
        Log.d(TAG, "onCreate: CRASH WHILE CREATING NOTIFICATION CHANNEL")
      }
    }
  }

  override fun onUnbind(intent: Intent?): Boolean {
    Log.d(TAG, "onUnbind: Service unbind")
    return super.onUnbind(intent)
  }

  override fun onServiceConnected() {
    super.onServiceConnected()
    startService(Intent(applicationContext, CleanupService::class.java))
    Log.d(TAG, "onServiceConnected: Service connected")
  }

  override fun onAccessibilityEvent(event: AccessibilityEvent?) {

    // FILTER WHITE LIST
    Log.i(TAG, "onAccessibilityEvent: packageName is ${event?.packageName}")
    for(element in whiteListPackagePatterns) {
      if(event?.packageName?.contains(element) == true) {
        Log.d(TAG, "onAccessibilityEvent: There we go it is white list element")
        return
      }
    }
    // SKIP OUR APPLICATION
    if(event?.packageName == applicationContext.packageName) {
      return
    }

      when(event?.eventType) {
        AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
          sendNotificationWithCurrentMillis("Drive Safely! Switch Application Detected!", NOTIFICATION_ID_SWITCH_APP)
        }
        AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
          sendNotificationWithCurrentMillis("Drive Safely! Typing Detected", NOTIFICATION_ID_TEXGING) }
        AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
          sendNotificationWithCurrentMillis("Drive Safely! Scroll Detected", NOTIFICATION_ID_SCROLLING)
        }
        else -> {
        }
      }
    }

  override fun onInterrupt() {
    Log.d(TAG, "onServiceConnected: Service interrupted")
  }

  fun sendNotificationWithCurrentMillis(message: String, notification_id: String) {

    val currentTime = System.currentTimeMillis()
    // SEND NOTIFICATION IF LAST SENT ONE BEFORE 5 SECOND
    if (currentTime - lastTimeNotificationSent > 5000) {
      Log.d(TAG, "sendNotificationWithCurrentMillis: Notification channel is ${notification_id}")
      val notification: Notification = NotificationCompat.Builder(applicationContext, notification_id)
        .setContentTitle("Danger Detected!")
        .setContentText(message)
        .setSmallIcon(android.R.drawable.stat_notify_error)
        .build()

        val notificationManager: NotificationManager =
          getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)

        lastTimeNotificationSent = currentTime
      }
  }
}
