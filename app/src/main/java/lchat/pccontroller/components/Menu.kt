package lchat.pccontroller.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import lchat.pccontroller.VolumeViewModel
import lchat.pccontroller.components.buttons.AgeButton
import lchat.pccontroller.components.buttons.AndroidStudioButton
import lchat.pccontroller.components.buttons.CloseButton
import lchat.pccontroller.components.buttons.CopyButton
import lchat.pccontroller.components.buttons.DisneyButton
import lchat.pccontroller.components.buttons.IntellijButton
import lchat.pccontroller.components.buttons.OperaButton
import lchat.pccontroller.components.buttons.SpotifyButton
import lchat.pccontroller.components.utils.MenuGradient
import lchat.pccontroller.yt.YoutubeButton

@Composable
fun MenuScreen(navController: NavController, volumeViewModel: VolumeViewModel) {


    val buttons: List<@Composable () -> Unit> = listOf(
        { SpotifyButton() },
        { AgeButton() },
        { YoutubeButton(navController) },
        { DisneyButton() },
        { CopyButton() },
        { CloseButton() },
        { IntellijButton() },
        { AndroidStudioButton() },
        { OperaButton() }
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
            VolumeUtils(volumeViewModel)
        }
    }
}
