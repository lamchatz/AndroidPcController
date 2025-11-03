package lchat.pccontroller.yt

data class YouTubeResponse(
    val items: List<YouTubeVideoItem>,
    val nextPageToken: String? = null
)

data class YouTubeVideoItem(
    val id: VideoId,
    val snippet: Snippet
)

data class VideoId(
    val videoId: String
)

data class Snippet(
    val title: String,
    val description: String,
    val thumbnails: Thumbnails
)

data class Thumbnails(
    val medium: ThumbnailDetails
)

data class ThumbnailDetails(
    val url: String
)
