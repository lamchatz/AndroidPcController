package lchat.pccontroller

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class RequestHandler {
    companion object {
        private const val BASE_URL = "http://192.168.2.7:9091/"
        private val client = OkHttpClient()

        private fun buildRequest(endpoint: String): Request {
            val body = "".toRequestBody("text/plain".toMediaTypeOrNull())
            return Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build()
        }

        private suspend fun makeRequest(endpoint: String): Boolean = withContext(Dispatchers.IO) {
            try {
                client.newCall(buildRequest(endpoint)).execute().use { response ->
                    if (!response.isSuccessful) return@use false

                    val body = response.body?.string()?.trim()
                    when (body?.lowercase()) {
                        "true" -> true
                        else -> false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        suspend fun openAge(): Boolean = makeRequest("age")

        suspend fun openSpotify(): Boolean = makeRequest("spotify")

    }
}