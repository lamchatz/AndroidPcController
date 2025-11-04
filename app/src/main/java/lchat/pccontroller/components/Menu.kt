package lchat.pccontroller.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lchat.pccontroller.R
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

            // Footer Row (bottom)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(50.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.volume_down),
                    contentDescription = "Volume down icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(64.dp)
                        .clickable {
                            executeRequest(
                                context = context,
                                scope = scope,
                                request = { RequestHandler.decrease() },
                                errorMessage = "Failed to decrease sound"
                            )
                        }
                )

                Icon(
                    painter = painterResource(id = R.drawable.mute_toggle),
                    contentDescription = "Mute toggle icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(64.dp)
                        .clickable {
                            executeRequest(
                                context = context,
                                scope = scope,
                                request = { RequestHandler.toggleMute() },
                                errorMessage = "Failed to toggle mute"
                            )
                        }
                )

                Icon(
                    painter = painterResource(id = R.drawable.volume_up),
                    contentDescription = "Volume up icon",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(64.dp)
                        .clickable {
                            executeRequest(
                                context = context,
                                scope = scope,
                                request = { RequestHandler.increase() },
                                errorMessage = "Failed to increase sound"
                            )
                        }
                )
            }
        }
    }

}