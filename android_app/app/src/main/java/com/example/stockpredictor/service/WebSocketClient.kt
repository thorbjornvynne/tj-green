package com.example.stockpredictor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.stockpredictor.util.NotificationHelper
import androidx.core.app.NotificationCompat
import okhttp3.*
import okio.ByteString

class WebSocketClient(private val context: Context) {

    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket
    private val channelId = "tjgreen_alerts"

    fun connect() {
        val request = Request.Builder()
            .url("ws://10.0.2.2:8000/ws/alerts")  // Replace with actual IP/domain if needed
            .build()
        webSocket = client.newWebSocket(request, TJGreenWebSocketListener())
    }

    private inner class TJGreenWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            // Connected successfully
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            NotificationHelper.showNotification(context, "Prediction Alert", text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            NotificationHelper.showNotification(context, "Prediction Alert", bytes.utf8())
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(1000, null)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            t.printStackTrace()
        }
    }

    // Moved to NotificationHelper
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "TJ-Green Alerts", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}