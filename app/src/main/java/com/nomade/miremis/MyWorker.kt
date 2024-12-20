package com.nomade.miremis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nomade.miremis.net.RetrofitService
import com.nomade.miremis.utils.Constants.RESERVA_KEY
import com.nomade.miremis.utils.Constants.USER_EMAIL_KEY
import com.nomade.miremis.utils.SharedPrefsUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class MyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private var mediaPlayer: MediaPlayer? = null

    override fun doWork(): Result {
        val mail = SharedPrefsUtil.get(USER_EMAIL_KEY, "")
        val reserva = SharedPrefsUtil.get(RESERVA_KEY, "")
        Log.w("REMIS MyWorker", "enviando - hayMensajes ${mail} ${reserva}")

        val call: Call<String?>? = RetrofitService.RetrofitInstance.api.hayMensajes(mail, reserva)
        call!!.enqueue(object : Callback<String?> {
            override fun onResponse(
                call: Call<String?>,
                response: Response<String?>
            ) {
                if (response.isSuccessful()) {
                    Log.w("REMIS MyWorker", "hayMensajes: ${response.body()}")
                    val movilData = response.body() ?: ""
                    if (!movilData.lowercase().contains("no hay mensaje")) {
                        playSound(applicationContext)
                        showNotification(applicationContext)
                    }

                }

            }

            override fun onFailure(call: Call<String?>, t: Throwable) {
                Log.w(
                    "REMIS MyWorker",
                    "hayMensajes: error - ${call.request()} , ${t.localizedMessage}"
                )
            }
        })

        // Cuando el trabajo termina, reprogramamos el siguiente request
        scheduleNextWork()

        return Result.success()
    }

    private fun scheduleNextWork() {
        // Construimos el OneTimeWorkRequest para que se ejecute después de 60 segundos
        val nextWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setInitialDelay(60, TimeUnit.SECONDS) // Configura el delay de 60 segundos
            .build()

        // Encolamos el siguiente trabajo
        WorkManager.getInstance(applicationContext).enqueue(nextWorkRequest)
    }

    private fun playSound(context: Context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.chime_notification_marimba)
        mediaPlayer?.setOnCompletionListener { // Use setOnCompletionListener
            mediaPlayer?.release()
            mediaPlayer = null
        }
        mediaPlayer?.start()
    }

    private fun showNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        Log.w("REMIS SERVICE", "NOTIFICATION")

        // Create a notification channel (only for Android 8.0 and superiores)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "remis_channel_id",
                "Remis",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Notifications from RemisCar" // Optional description
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        // Reemplaza MainActivity con tu actividad principal
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            context,
            "remis_channel_id"
        )
            .setContentTitle("RemisCar")
            .setContentText("Tiene un nuevo mensaje")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }
}