package lchat.pccontroller

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

object MouseWebSocketClient {

    private var ws: WebSocket? = null

    fun connect(serverUrl: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(serverUrl).build()

        ws = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                println("WebSocket connected")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                println("Message from server: $text")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                t.printStackTrace()
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("WebSocket closed: $reason")
            }
        })
    }

    fun sendAction(json: JSONObject) {
        println(json)
        ws?.send(json.toString())
    }

    fun close() {
        ws?.close(1000, "App closed")
    }
}
