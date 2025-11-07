package lchat.pccontroller.components.buttons

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import lchat.pccontroller.RequestHandler

@Composable
fun BaseIconButton(
    icon: Painter,
    containerColor: Color,
    iconTint: Color = Color.Unspecified,
    size: Dp = 88.dp,
    onClick: suspend (context: Context) -> Boolean,
    onDoubleClick: (suspend (context: Context) -> Boolean)? = null,
    onLongPress: (suspend (context: Context) -> Boolean)? = null,
    successMessage: String? = null,
    longPressMessage: String? = null,
    scale: Float = 0.8f
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .size(size)
            .background(color = containerColor, shape = CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        scope.launch {
                            RequestHandler.executeRequest(
                                context = context,
                                scope = scope,
                                request = { onClick(context) },
                                successMessage = successMessage
                            )
                        }
                    },
                    onDoubleTap = {
                        if (onDoubleClick != null) {
                            scope.launch {
                                RequestHandler.executeRequest(
                                    context = context,
                                    scope = scope,
                                    request = { onDoubleClick(context) }
                                )
                            }
                        }
                    },
                    onLongPress = {
                        if (onLongPress != null) {
                            scope.launch {
                                RequestHandler.executeRequest(
                                    context = context,
                                    scope = scope,
                                    request = { onLongPress(context) },
                                    successMessage = longPressMessage
                                )
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = "Button icon",
            tint = iconTint,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxWidth(scale)
                .fillMaxHeight(scale)
        )
    }
}
