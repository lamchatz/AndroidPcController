package lchat.pccontroller.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lchat.pccontroller.RequestHandler
import lchat.pccontroller.RequestHandler.Companion.executeRequest


@Composable
fun Menu() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") {
            MenuScreen(navController)
        }


        composable(
            "youtube_search",
            enterTransition = youtubeEnterTransition,
            exitTransition = youtubeExitTransition
        ) {
            YoutubeSearchScreen()
        }
    }
}

@Composable
fun MenuScreen(navController: NavController) {
    var sliderValue by remember { mutableStateOf(50f) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = MenuGradient),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                SpotifyButton()
                AgeButton()
                YoutubeButton(navController)
                DisneyButton()
                CopyButton()
            }

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
                    modifier = Modifier
                        .fillMaxWidth(0.8f),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF8C5DFD),             // the draggable thumb
                        activeTrackColor = Color(0xFF39DDF8),     // track before the thumb
                        inactiveTrackColor = Color(0xFF1A7B86)      // track after the thumb
                    )
                )

                // Volume icon row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(
                        24.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VolumeRow()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 64.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VolumeRow()
            }
        }
    }

}