package lchat.pccontroller.components

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lchat.pccontroller.components.utils.trackpadEnterTransition
import lchat.pccontroller.components.utils.trackpadExitTransition
import lchat.pccontroller.components.utils.youtubeEnterTransition
import lchat.pccontroller.components.utils.youtubeExitTransition

@Composable
fun AppNavHost() {
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


        composable(
            "trackpad",
            enterTransition = trackpadEnterTransition,
            exitTransition = trackpadExitTransition
        ) {
            TrackpadScreen(navController)
        }
    }
}
