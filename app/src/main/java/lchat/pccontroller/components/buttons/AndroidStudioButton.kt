package lchat.pccontroller.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun AndroidStudioButton() {
    BaseIconButton(
        icon = painterResource(id = R.drawable.android),
        containerColor = Color.White,
        onClick = {
            RequestHandler.openAndroidStudio()
        },
        successMessage = "Opening Android Studio"
    )
}