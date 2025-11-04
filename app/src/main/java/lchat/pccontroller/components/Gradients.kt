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

val CopyMenu = Brush.radialGradient(
    colors = listOf(
        Color(0xFFA88FC5), // hsla(261, 35%, 66%, 1)
        Color(0xFF9990B3), // hsla(260, 24%, 60%, 1)
        Color(0xFF58517A)  // hsla(260, 41%, 45%, 1)
    ),
    center = Offset.Unspecified,
    radius = Float.POSITIVE_INFINITY
)

val MenuGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF211F2F), Color(0xFF918CA9)),
    start = Offset(0f, 0f),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)