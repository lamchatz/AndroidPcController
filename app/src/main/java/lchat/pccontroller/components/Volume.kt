package lchat.pccontroller.components

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
fun VolumeUtils() {
    var sliderValue by remember { mutableStateOf(50f) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Slider(
        value = sliderValue,
        onValueChange = { sliderValue = it },
        valueRange = 0f..100f,
        steps = 0,
        onValueChangeFinished = {
            executeRequest(
                context = context,
                scope = scope,
                request = { RequestHandler.setVolume(sliderValue) },
                errorMessage = "Failed to adjust volume!"
            )
        },
        modifier = Modifier.fillMaxWidth(0.8f),
        colors = SliderDefaults.colors(
            thumbColor = Color(0xFF8C5DFD),
            activeTrackColor = Color(0xFF39DDF8),
            inactiveTrackColor = Color(0xFF1A7B86)
        )
    )

    VolumeRow(
        sliderValue = sliderValue,
        onSliderValueChange = { sliderValue = it },
        context = context,
        scope = scope
    )
}

private const val volumeChangeValue = 3.05f

@Composable
fun VolumeRow(
    sliderValue: Float,
    onSliderValueChange: (Float) -> Unit,
    context: Context = LocalContext.current,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VolumeIcon(
            iconRes = R.drawable.half_volume,
            description = "Half volume",
            context = context,
            scope = scope,
            request = {
                onSliderValueChange(50f)
                RequestHandler.halfSound() },

            errorMessage = "Failed to set half volume"
        )

        VolumeIcon(
            iconRes = R.drawable.volume_down,
            description = "Volume down icon",
            context = context,
            scope = scope,
            errorMessage = "Failed to decrease sound",
            request = {
                onSliderValueChange((sliderValue - volumeChangeValue).coerceAtMost(100f))
                RequestHandler.decrease()
            }
        )

        VolumeIcon(
            iconRes = R.drawable.mute_toggle,
            description = "Mute toggle",
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
            request = {
                onSliderValueChange((sliderValue + volumeChangeValue).coerceAtMost(100f))
                RequestHandler.increase()
            },
            errorMessage = "Failed to increase sound"
        )

        VolumeIcon(
            iconRes = R.drawable.max_volume,
            description = "Max volume",
            context = context,
            scope = scope,
            request = {
                onSliderValueChange(100f)
                RequestHandler.maxSound() },

            errorMessage = "Failed to set max volume"
        )
    }
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
