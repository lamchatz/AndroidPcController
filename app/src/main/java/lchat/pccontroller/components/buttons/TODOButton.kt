package lchat.pccontroller.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun TODOButton() {
    BaseIconButton(
        icon = painterResource(id = R.drawable.todo),
        containerColor = Color.White,
        onClick = {
            RequestHandler.openTODO()
        },
        successMessage = "Opening TODO app"
    )
}