package lchat.pccontroller.components.buttons

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun AgeButton() {
    BaseIconButton(
        icon = painterResource(id = R.drawable.age_logo),
        containerColor = MaterialTheme.colorScheme.secondary,
        onClick = {
            RequestHandler.openAge()
        },
        scale = 1f,
        successMessage = "Opening Age of Mythology and Discord"
    )
}