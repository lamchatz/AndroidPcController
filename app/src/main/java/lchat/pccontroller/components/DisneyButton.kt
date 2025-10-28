package lchat.pccontroller.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun DisneyButton() {
    BaseButton(
        text = "Disney+",
        msg = "Opening Disney+",
        icon = painterResource(id = R.drawable.disney),
        containerColor = Color(0xFF113CCF),
        onClick = {
            RequestHandler.openDisney()
        }
    )
}