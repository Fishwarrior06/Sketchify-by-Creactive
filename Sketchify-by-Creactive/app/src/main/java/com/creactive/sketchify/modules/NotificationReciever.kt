// Archivo: com/creactive/sketchify/modules/NotificationReceiver.kt
package com.creactive.sketchify.modules

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.creactive.sketchify.MainActivity
import com.creactive.sketchify.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationReceiver", "Alarma recibida. Mostrando notificación.")
        // 1. Mostrar la notificación
        showNotification(context)
    }

    private fun showNotification(context: Context) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "reminder_channel_id")
            .setSmallIcon(R.drawable.ic_notification_icon) // <-- ASEGÚRATE de que este ícono existe
            .setContentTitle("¡Es hora de ser creativo!")
            .setContentText("No olvides usar Sketchify hoy para capturar tus ideas.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(101, notification)
    }

    companion object {
        fun scheduleRandomAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                1, // Usar un ID único para este PendingIntent
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // La lógica para calcular la hora aleatoria no cambia
            val calendar = Calendar.getInstance()

            // Para probarlo rápidamente, programemos la alarma para dentro de 15 segundos
            val triggerAtMillis = System.currentTimeMillis() + 15_000 // 15 segundos
            calendar.timeInMillis = triggerAtMillis

            // Usamos set() en lugar de setExactAndAllowWhileIdle().
            // No requiere permisos especiales y es ideal para recordatorios que no son críticos en el tiempo.
            // El sistema operativo puede ajustar ligeramente la hora de entrega para ahorrar batería.
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            val formattedTime = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(calendar.time)
            Log.d("NotificationScheduler", "Próximo recordatorio (inexacto) programado para: $formattedTime")
        }
    }
}