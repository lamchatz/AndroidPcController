package lchat.pccontroller.components.buttons

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun NetflixButton() {
    BaseIconButton(
        icon = painterResource(R.drawable.netflix),
        containerColor = Color.Black,
        onClick = {
            RequestHandler.openNetflix();
        },
        successMessage = "Opening Netflix"

    )
}