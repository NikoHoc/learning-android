package com.dicoding.dicodingevent.ui.settings

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.provider.UserDictionary.Words.APP_ID
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.remote.response.EventResponse
import com.dicoding.dicodingevent.data.remote.retrofit.ApiConfig
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.SyncHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    companion object {
        private val TAG = MyWorker::class.java.simpleName
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "dicoding channel"
    }

    override fun doWork(): Result {
        return getNearbyEvent()
    }

    private fun getNearbyEvent(): Result {
        Log.d(TAG, "getNearbyEvent: Mulai.....")
        val client = ApiConfig.getApiService().getNearbyEvent()

        client.enqueue(object: Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val name: String = response.body()?.listEvents?.get(0)?.name.orEmpty()
                    val beginTime: String = response.body()?.listEvents?.get(0)?.beginTime.orEmpty()
                    val link: String = response.body()?.listEvents?.get(0)?.link.orEmpty()
                    showNotification(name, beginTime, link)
                    Log.d(TAG, "onSuccess: Selesai.....")
                } else {
                    showNotification("Get Nearby Event Not Success", "no date", "no link")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: Gagal.....")
                showNotification("Get Nearby Event Failed", "no date", "no link")
            }
        })
        return Result.success()
    }

    private fun showNotification(eventName: String, beginTime: String, link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notif)
            .setContentTitle(eventName)
            .setContentText(beginTime)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}