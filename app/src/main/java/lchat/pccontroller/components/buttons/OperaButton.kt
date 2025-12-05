package lchat.pccontroller.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun OperaButton() {
    BaseIconButton(
        icon = painterResource(id = R.drawable.opera),
        containerColor = Color.Black,
        onClick = {
            RequestHandler.openOpera()
        },
        successMessage = "Opening Opera GX"
    )
}