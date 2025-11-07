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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import lchat.pccontroller.RequestHandler
import lchat.pccontroller.RequestHandler.Companion.executeRequest
import lchat.pccontroller.components.buttons.AgeButton
import lchat.pccontroller.components.buttons.CloseButton
import lchat.pccontroller.components.buttons.CopyButton
import lchat.pccontroller.components.buttons.DisneyButton
import lchat.pccontroller.components.buttons.SpotifyButton
import lchat.pccontroller.components.utils.MenuGradient
import lchat.pccontroller.yt.YoutubeButton

@Composable
fun MenuScreen(navController: NavController) {


    val buttons: List<@Composable () -> Unit> = listOf(
        { SpotifyButton() },
        { AgeButton() },
        { YoutubeButton(navController) },
        { DisneyButton() },
        { CopyButton() },
        { CloseButton() }
    )

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
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            items(buttons.size) { index ->
                buttons[index]()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VolumeUtils()
        }
    }
}

@Composable
@Preview
fun MenuPreview() {
    val navController = rememberNavController()

    MenuScreen(navController)
}
