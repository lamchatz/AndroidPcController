package lchat.pccontroller.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import lchat.pccontroller.RequestHandler

@Composable
fun AgeButton() {
    BaseButton(
        text = "Age of Mythology",
        msg = "Opening Age of Mythology and Discord",
        containerColor = MaterialTheme.colorScheme.secondary,
        textColor = MaterialTheme.colorScheme.onSecondary,
        onClick = {
            RequestHandler.openAge()
        }
    )
}