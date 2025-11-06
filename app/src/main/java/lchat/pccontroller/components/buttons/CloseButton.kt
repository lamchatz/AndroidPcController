package lchat.pccontroller.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun CloseButton() {
    BaseIconButton(
        icon = painterResource(id = R.drawable.close),
        containerColor = Color(0xFFF44336),
        scale = 1f,
        onClick = {
            RequestHandler.close()
        }
    )
}