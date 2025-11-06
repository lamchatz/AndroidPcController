package lchat.pccontroller.components.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

const val POPUP_ANIMATION_DURATION = 300
const val MENU_ANIMATION_DURATION = 250


val DialogEnterAnimation =
    scaleIn(initialScale = 0.8f, animationSpec = tween(POPUP_ANIMATION_DURATION + 200)) + fadeIn(
        animationSpec = tween(POPUP_ANIMATION_DURATION + 200)
    )

val DialogExitAnimation = scaleOut(animationSpec = tween(POPUP_ANIMATION_DURATION)) + fadeOut(
    animationSpec = tween(POPUP_ANIMATION_DURATION)
)

val youtubeEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
        fadeIn(
            animationSpec = tween(
                MENU_ANIMATION_DURATION, easing = LinearEasing
            )
        ) + slideIntoContainer(
            animationSpec = tween(MENU_ANIMATION_DURATION, easing = EaseIn),
            towards = AnimatedContentTransitionScope.SlideDirection.Start
        )
    }

val youtubeExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
        fadeOut(
            animationSpec = tween(
                MENU_ANIMATION_DURATION, easing = LinearEasing
            )
        ) + slideOutOfContainer(
            animationSpec = tween(MENU_ANIMATION_DURATION, easing = EaseOut),
            towards = AnimatedContentTransitionScope.SlideDirection.End
        )
    }

val trackpadEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
    {
        slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    }
val trackpadExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
    {
        slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        )
    }