// En BootCompletedReceiver.kt
package com.creactive.sketchify.data // O tu paquete correcto

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.creactive.sketchify.modules.NotificationReceiver

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            Log.d("BootReceiver", "Dispositivo reiniciado. Reprogramando alarma aleatoria.")
            // Simplemente llamamos a nuestra función centralizada
            NotificationReceiver.scheduleRandomAlarm(context)
        }
    }
}