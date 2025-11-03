package lchat.pccontroller.components// Assuming your BaseButton and lchat.pccontroller.components.YoutubeButton (with popup logic) are defined as before
// and R.drawable.youtube is available.


import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

private const val ANIMATION_DURATION = 300

val Gradient = Brush.radialGradient(
    colors = listOf(Color(0xFFF03030), Color(0xFFB6353B)),
    center = Offset.Unspecified,
    radius = Float.POSITIVE_INFINITY
)
val DialogEnterAnimation =
    scaleIn(initialScale = 0.8f, animationSpec = tween(ANIMATION_DURATION + 200)) + fadeIn(
        animationSpec = tween(ANIMATION_DURATION + 200)
    )


val DialogExitAnimation = scaleOut(animationSpec = tween(ANIMATION_DURATION)) + fadeOut(
    animationSpec = tween(ANIMATION_DURATION)
)

@Composable
fun YoutubeButton() {
    var showDialog by remember { mutableStateOf(false) }
    var animateDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val close = {
        animateDialog = false
        coroutineScope.launch {
            delay(ANIMATION_DURATION.toLong())
            showDialog = false
        }
    }

    BaseButton(
        text = "Youtube",
        icon = painterResource(id = R.drawable.youtube),
        containerColor = Color(0xFFFF0000),
        onClick = {
            showDialog = true
            true
        }
    )

    if (showDialog) {
        LaunchedEffect(Unit) {
            animateDialog = true
        }

        YoutubeOptionsDialog(
            visible = animateDialog,
            onDismiss = {
                close()
            },
            onHomeClicked = {
                RequestHandler.executeRequest(
                    context = context,
                    scope = coroutineScope,
                    request = { RequestHandler.openYoutube() },
                    successMessage = "Opening Youtube!"
                )
                close()
            },
            onSearchClicked = {
                close()
            }
        )
    }
}

@Composable
fun YoutubeOptionsDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onHomeClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {
        AnimatedVisibility(
            visible = visible,
            enter = DialogEnterAnimation,
            exit = DialogExitAnimation
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        brush = Gradient,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Youtube",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PopupItemWithIcon(
                        text = "Home",
                        icon = painterResource(id = R.drawable.home),
                        onClick = onHomeClicked,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    PopupItemWithIcon(
                        text = "Search",
                        icon = painterResource(id = R.drawable.search),
                        onClick = onSearchClicked,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

