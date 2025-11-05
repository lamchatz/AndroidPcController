package lchat.pccontroller.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300, easing = FastOutSlowInEasing)
                )
            }
        ) {
            TrackpadScreen(navController)
        }
    }
}
