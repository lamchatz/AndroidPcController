package lchat.pccontroller.components

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler
import lchat.pccontroller.RequestHandler.Companion.executeRequest

@Composable
fun VolumeRow(
    context: Context = LocalContext.current,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    VolumeIcon(
        iconRes = R.drawable.half_volume,
        description = "Half volume icon",
        context = context,
        scope = scope,
        request = { RequestHandler.halfSound() },
        errorMessage = "Failed to increase sound to max"
    )

    VolumeIcon(
        iconRes = R.drawable.volume_down,
        description = "Volume down icon",
        context = context,
        scope = scope,
        request = { RequestHandler.decrease() },
        errorMessage = "Failed to decrease sound"
    )

    VolumeIcon(
        iconRes = R.drawable.mute_toggle,
        description = "Mute toggle icon",
        context = context,
        scope = scope,
        request = { RequestHandler.toggleMute() },
        errorMessage = "Failed to toggle mute"
    )

    VolumeIcon(
        iconRes = R.drawable.volume_up,
        description = "Volume up icon",
        context = context,
        scope = scope,
        request = { RequestHandler.increase() },
        errorMessage = "Failed to increase sound"
    )

    VolumeIcon(
        iconRes = R.drawable.max_volume,
        description = "Max volume icon",
        context = context,
        scope = scope,
        request = { RequestHandler.maxSound() },
        errorMessage = "Failed to increase sound to max"
    )
}

@Composable
fun VolumeIcon(
    @DrawableRes iconRes: Int,
    description: String,
    context: Context,
    scope: CoroutineScope,
    request: suspend () -> Boolean,
    errorMessage: String
) {
    Icon(
        painter = painterResource(id = iconRes),
        contentDescription = description,
        tint = Color.Black,
        modifier = Modifier
            .size(56.dp)
            .clickable {
                executeRequest(
                    context = context,
                    scope = scope,
                    request = request,
                    errorMessage = errorMessage
                )
            }
    )
}
