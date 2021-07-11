package com.pnhung.icarchecking

import android.util.Log
import com.google.gson.Gson
import com.pnhung.icarchecking.view.MapManager
import com.pnhung.icarchecking.view.api.model.entities.MessageEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI

class WebSocketUtil private constructor() {
    private var mWebSocketClient: WebSocketClient? = null
    fun connectWebSocket() {
        if (mWebSocketClient != null && !mWebSocketClient!!.isClosed) {
            return
        }
        val uri: URI = try {
            val token = CommonUtils.getInstance().getPref(TOKEN)
            if (token == null || token.isEmpty()) {
                return
            }
            val mToken = String.format(SERVER_ADDRESS, token)
            Log.i(TAG, "token: $mToken")
            URI.create(mToken)
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        mWebSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.i(TAG, "WebSocket: Opened")
                sendMessage()
            }

            @Synchronized
            override fun onMessage(message: String?) {
                Log.i(TAG, message!!)
                if (message.contains(TYPE_PING) || message.contains(TYPE_WELCOME)) {
                    return
                }
                //Xử lý sms
                val carInfo = Gson().fromJson(message, MessageEntity::class.java)

                if (carInfo.carInfo == null) return
                GlobalScope.launch(Dispatchers.Main) {
                    MapManager.getInstance().updateStatusCar(carInfo.carInfo)
                    MapManager.getInstance().updateTrackingCar(carInfo.carInfo)
                }
                Log.i(TAG, "WebSocket : sms: ${carInfo.carInfo}")
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.i("WebSocket", "Closed $reason")
            }

            override fun onError(ex: Exception) {
                Log.i("WebSocket", "Error " + ex.message)
            }

        }
        mWebSocketClient?.connect()
    }

    private fun sendMessage() {
        if (mWebSocketClient == null) {
            return
        }
        mWebSocketClient?.send("{\"command\":\"subscribe\",\"identifier\":\"{\\\"channel\\\":\\\"ClientsChannel\\\"}\"}")
    }

    fun disconnect() {
        if (mWebSocketClient != null) {
            mWebSocketClient?.close()
            mWebSocketClient = null
            instance = null
        }
    }

    companion object {
        const val TOKEN = "TOKEN"
        private const val SERVER_ADDRESS =
            "wss://icar-api.techja.edu.vn/socket-io?Authorization=Bearer__%s"
        private val TAG = WebSocketUtil::class.java.name
        private const val TYPE_PING = "\"type\":\"ping\""
        private const val TYPE_WELCOME = "\"type\":\"welcome\""
        private var instance: WebSocketUtil? = null
        fun getInstance(): WebSocketUtil? {
            if (instance == null) {
                instance = WebSocketUtil()
            }
            return instance
        }
    }
}