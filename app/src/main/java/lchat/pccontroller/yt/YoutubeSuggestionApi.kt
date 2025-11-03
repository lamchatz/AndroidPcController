package lchat.pccontroller.yt

import retrofit2.http.GET
import retrofit2.http.Query

interface YouTubeSuggestionApi {
    @GET("complete/search?client=firefox&ds=yt")
    suspend fun getSuggestions(@Query("q") query: String): List<Any>
}