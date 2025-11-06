package lchat.pccontroller.components.buttons

import android.content.Context
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import lchat.pccontroller.RequestHandler


@Composable
fun BaseButton(
    text: String,
    msg: String? = null,
    longPressMsg: String ?= null,
    containerColor: Color,
    textColor: Color = Color.White,
    icon: Painter? = null,
    width: Dp = 250.dp,
    height: Dp = 60.dp,
    shape: Shape = RoundedCornerShape(16.dp),
    onClick: suspend (context: Context) -> Boolean,
    onDoubleClick: (suspend (context: Context) -> Boolean)? = null,
    onLongPress: (suspend (context: Context) -> Boolean)? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Surface(
        shape = shape,
        color = containerColor,
        modifier = Modifier
            .width(width)
            .height(height)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        scope.launch {
                            RequestHandler.executeRequest(
                                context = context,
                                scope = scope,
                                request = { onClick(context) },
                                successMessage = msg
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
                                    successMessage = longPressMsg
                                )
                            }
                        }
                    }
                )
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = "$text logo",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(72.dp)
                        .padding(end = 16.dp)
                )
            }
            Text(
                text = text,
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

