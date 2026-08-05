package lchat.pccontroller.components.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler


@Composable
fun StremioButton() {
    BaseIconButton(
        icon = painterResource(id = R.drawable.stremio),
        containerColor = MaterialTheme.colorScheme.secondary,
        onClick = {
            RequestHandler.openStremio()
        },
        scale = 1f,
        successMessage = "Opening Stremio"
    )
}