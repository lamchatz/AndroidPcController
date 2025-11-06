package lchat.pccontroller.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import lchat.pccontroller.RequestHandler
import lchat.pccontroller.RequestHandler.Companion.executeRequest
import lchat.pccontroller.components.buttons.AgeButton
import lchat.pccontroller.components.buttons.BaseButton
import lchat.pccontroller.components.buttons.CopyButton
import lchat.pccontroller.components.buttons.DisneyButton
import lchat.pccontroller.components.buttons.SpotifyButton
import lchat.pccontroller.components.utils.MenuGradient


@Composable
fun MenuScreen(navController: NavController) {
    var sliderValue by remember { mutableStateOf(50f) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MenuGradient)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 20) {
                        if (navController.currentDestination?.route != "trackpad") {
                            navController.navigate("trackpad") {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SpotifyButton()
            AgeButton()
            YoutubeButton(navController)
            DisneyButton()
            CopyButton()
            BaseButton(
                text = "Close",

                containerColor = Color.Red,
                onClick = {
                    RequestHandler.close()
                }
            )
        }

        // Bottom volume slider
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VolumeRow()
            }
        }
    }
}
