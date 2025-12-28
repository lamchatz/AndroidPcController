package lchat.pccontroller

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class RequestHandler {
    companion object {
        private const val BASE_URL = "http://192.168.1.11:9091/"
        private val client = OkHttpClient()

        private fun buildPostRequest(endpoint: String): Request {
            val body = "".toRequestBody("text/plain".toMediaTypeOrNull())
            return Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build()
        }

        private fun buildGetRequest(endpoint: String): Request {
            return Request.Builder()
                .url(BASE_URL + endpoint)
                .get()
                .build()
        }

        private suspend fun makeRequest(endpoint: String): Boolean = withContext(Dispatchers.IO) {
            try {
                client.newCall(buildPostRequest(endpoint)).execute().use { response ->
                    if (!response.isSuccessful) return@use false

                    val body = response.body?.string()?.trim()
                    when (body?.lowercase()) {
                        "true" -> true
                        else -> false
                    }
                }
            } catch (e: Exception) {
                Log.e("PcController", "Request failed", e)
                false
            }
        }

        private suspend fun makeReadRequest(endpoint: String): String =
            withContext(Dispatchers.IO) {
                try {
                    client.newCall(buildGetRequest(endpoint)).execute().use { response ->
                        if (!response.isSuccessful) return@use "error"

                        val body = response.body?.string()?.trim() ?: return@use "empty"

                        val json = JSONObject(body)
                        if (json.optBoolean("success", false)) {
                            json.optString("output", "empty")
                        } else {
                            Log.e(
                                "PcController",
                                "Command failed: ${json.optString("error", "unknown error")}"
                            )
                            "error"
                        }
                    }
                } catch (e: Exception) {
                    Log.e("PcController", "Request failed", e)
                    "error"
                }
            }

        suspend fun openAge(): Boolean = makeRequest("open/age")

        suspend fun openSpotify(): Boolean = makeRequest("open/spotify")

        suspend fun openYoutube(): Boolean = makeRequest("open/youtube")
        suspend fun openYoutubeVideo(videoId: String): Boolean =
            makeRequest("open/youtube?videoId=$videoId")

        suspend fun openDisney(): Boolean = makeRequest("open/disney")
        suspend fun openNetflix(): Boolean = makeRequest("open/netflix")

        suspend fun openIntellij(): Boolean = makeRequest("open/intellij")
        suspend fun openAndroidStudio(): Boolean = makeRequest("open/android")
        suspend fun openOpera(): Boolean = makeRequest("open/opera")
        suspend fun openTODO(): Boolean = makeRequest("open/todo")

        suspend fun toggleMute(): Boolean = makeRequest("sound/toggle")

        suspend fun maxSound(): Boolean = makeRequest("sound/max")

        suspend fun halfSound(): Boolean = makeRequest("sound/half")

        suspend fun increase(): Boolean = makeRequest("sound/increase")

        suspend fun decrease(): Boolean = makeRequest("sound/decrease")

        suspend fun setVolume(arg: Float): Boolean = makeRequest("sound/set?arg=$arg")

        suspend fun turnMonitorOff(): Boolean = makeRequest("turnMonitorOff")

        suspend fun paste(text: String): Boolean = makeRequestWithBody("paste", text)
        suspend fun copy(): String = makeReadRequest("copy")

        suspend fun close(): Boolean = makeRequest("close")

        fun executeRequest(
            context: Context,
            scope: CoroutineScope,
            request: suspend () -> Boolean,
            successMessage: String? = null,
            errorMessage: String = "Something went wrong"
        ) {
            scope.launch {
                val result = runCatching { request() }.getOrElse { false }
                val message = if (result) {
                    successMessage ?: ""
                } else {
                    errorMessage
                }

                if (message.isNotEmpty()) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        private suspend fun makeRequestWithBody(endpoint: String, body: String): Boolean =
            withContext(Dispatchers.IO) {
                try {
                    val requestBody = body.toRequestBody("text/plain".toMediaType())
                    val request = Request.Builder()
                        .url(BASE_URL + endpoint)
                        .post(requestBody)
                        .build()

                    client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful) return@use false
                        response.body?.string()?.trim()?.lowercase() == "true"
                    }
                } catch (e: Exception) {
                    Log.e("PcController", "Request failed", e)
                    false
                }
            }
    }
}