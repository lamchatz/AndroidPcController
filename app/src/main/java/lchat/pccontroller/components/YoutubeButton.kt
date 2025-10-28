package lchat.pccontroller.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun YoutubeButton() {
    BaseButton(
        text = "Youtube",
        msg = "Opening Youtube!",
        icon = painterResource(id = R.drawable.youtube),
        containerColor = Color(0xFFFF0000),
        onClick = {
            RequestHandler.openYoutube()
        }
    )
}