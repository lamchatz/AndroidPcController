package lchat.pccontroller.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun IntellijButton() {
    BaseIconButton(
        icon = painterResource(id = R.drawable.intellij),
        containerColor = Color.Black,
        onClick = {
            RequestHandler.openIntellij()
        },
        successMessage = "Opening Intellij"
    )
}