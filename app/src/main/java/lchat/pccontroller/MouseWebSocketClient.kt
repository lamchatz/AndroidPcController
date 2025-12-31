package lchat.pccontroller

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import lchat.pccontroller.data.repo.PcRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

object MouseWebSocketClient {

    private var ws: WebSocket? = null
    private val client = OkHttpClient()
    private var repoJob: Job? = null

    /**
     * Initialize the WebSocket client with the repository
     */
    fun init(pcRepository: PcRepository, context: Context) {
        repoJob?.cancel()
        repoJob = CoroutineScope(Dispatchers.IO).launch {
            pcRepository.baseUrl.collect { url ->
                // Only attempt if URL is valid
                if (pcRepository.DEFAULT_BASE_URL != url) {
                    reconnect(url + "mouseKeyboard", context)
                }
            }
        }
    }

    private fun reconnect(serverUrl: String, context: Context) {
        try {
            ws?.close(1000, "Switching server")
            ws = null

            val request = Request.Builder().url(serverUrl).build()
            ws = client.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    println("WebSocket connected to $serverUrl")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    println("Message from server: $text")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    t.printStackTrace()
                    // Show toast safely
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            "WebSocket connection failed: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    println("WebSocket closed: $reason")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    "Error connecting WebSocket: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun sendAction(json: JSONObject) {
        ws?.send(json.toString())
    }

    fun close() {
        ws?.close(1000, "App closed")
    }
}
