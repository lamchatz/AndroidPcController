package lchat.pccontroller.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val YoutubeMenu = Brush.radialGradient(
    colors = listOf(Color(0xFFF03030), Color(0xFFB6353B)),
    center = Offset.Unspecified,
    radius = Float.POSITIVE_INFINITY
)

val YoutubeSearch = Brush.linearGradient(
    colors = listOf(
        Color(0xFFF50404),
        Color(0xFFE41515)
    ),
    start = Offset(0f, 0f),
    end = Offset(0f, Float.POSITIVE_INFINITY)
)