package lchat.pccontroller.yt

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class YouTubeViewModel : ViewModel() {
    private val _videos = MutableStateFlow<List<YouTubeVideoItem>>(emptyList())
    val videos: StateFlow<List<YouTubeVideoItem>> = _videos

    private val apiKey = "AIzaSyDrNsQgRpY3gVQlcmJSfiPaRuxJEU3Iaps"

    fun searchVideos(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.searchVideos(
                    query = query,
                    apiKey = apiKey,
                    maxResults = 20
                )

                _videos.value = response.items

            } catch (e: Exception) {
                Log.e("YouTubeViewModel", "Error fetching videos", e)
            }
        }
    }

}