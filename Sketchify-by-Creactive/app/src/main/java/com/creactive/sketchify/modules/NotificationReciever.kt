// En NotificationReceiver.kt
package com.creactive.sketchify.modules // O tu paquete correcto

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.creactive.sketchify.MainActivity
import com.creactive.sketchify.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 1. Mostrar la notificación
        showNotification(context)
        // 2. Reprogramar la siguiente alarma
        scheduleRandomAlarm(context)
    }

    private fun showNotification(context: Context) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, activityIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, "reminder_channel_id")
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("¡Es hora de ser creativo!")
            .setContentText("No olvides usar Sketchify hoy para capturar tus ideas.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(101, notification)
    }

    // Usamos un companion object para que la función de programar sea accesible desde cualquier parte
    companion object {
        fun scheduleRandomAlarm(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // ---> LA SOLUCIÓN ESTÁ AQUÍ <---
            // Verificamos si podemos programar alarmas exactas (solo necesario para Android 12+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                // No tenemos permiso. No podemos usar setExactAndAllowWhileIdle.
                // Podríamos guiar al usuario a los ajustes o simplemente no hacer nada.
                Log.w("NotificationScheduler", "No se puede programar alarma exacta, permiso denegado.")
                Toast.makeText(context, "Por favor, concede el permiso de 'Alarmas y recordatorios' para recibir notificaciones.", Toast.LENGTH_LONG).show()
                // Opcional: Abrir los ajustes para que el usuario dé el permiso
                // val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                // context.startActivity(intent)
                return
            }

            val intent = Intent(context, NotificationReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                1,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // La lógica para calcular la hora aleatoria no cambia
            val calendar = Calendar.getInstance()
            val randomHour = Random.nextInt(9, 22)
            val randomMinute = Random.nextInt(0, 60)

            // Si estamos reprogramando, siempre es para el día siguiente.
            // Si es la primera vez, podría ser para hoy.
            val now = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, randomHour)
            calendar.set(Calendar.MINUTE, randomMinute)
            calendar.set(Calendar.SECOND, 0)
            if (calendar.before(now)) {
                calendar.add(Calendar.DAY_OF_YEAR, 1) // Si ya pasó la hora, programar para mañana
            }

            // Ahora sí podemos llamar a setExactAndAllowWhileIdle de forma segura
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )

            val formattedTime = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(calendar.time)
            Log.d("NotificationScheduler", "Próximo recordatorio programado para: $formattedTime")
        }
    }
}