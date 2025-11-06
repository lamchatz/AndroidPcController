package lchat.pccontroller.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import lchat.pccontroller.MouseWebSocketClient
import lchat.pccontroller.R
import lchat.pccontroller.components.utils.MenuGradient
import org.json.JSONObject
import kotlin.math.abs
import kotlin.math.roundToInt

enum class MouseActionType {
    CLICK,
    MOVE,
    SCROLL,
    TYPE
}

@Composable
@Preview
fun prevTracpadScreen() {
    TrackpadScreen()
}

@Composable
fun TrackpadScreen() {
    println("opening trackpad")

    var showTextField by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MenuGradient)
    ) {
        Trackpad { event ->
            MouseWebSocketClient.sendAction(event)
        }

        // Floating button with icon
        SmallFloatingActionButton(
            onClick = { showTextField = true },
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.BottomEnd)
                .offset(
                    x = (-8
                            ).dp, y = (-64).dp
                ) // move a bit higher
                .padding(16.dp),
            shape = CircleShape,
            containerColor = Color(0xFF6FD573),
            contentColor = Color.DarkGray,
            elevation = FloatingActionButtonDefaults.elevation(4.dp)
        ) {
            Icon(painterResource(id = R.drawable.space), contentDescription = "Text Input")
        }

        if (showTextField) {
            // Overlay TextField
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000)) // semi-transparent overlay
                    .clickable {
                        showTextField = false
                        focusManager.clearFocus()
                    }, // dismiss on outside tap
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Type here") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (text.isNotBlank()) {
                                val json = JSONObject().apply {
                                    put("action", MouseActionType.TYPE)
                                    put("text", text)
                                }
                                MouseWebSocketClient.sendAction(json)
                            }
                            showTextField = false
                            focusManager.clearFocus()
                            text = ""
                        }
                    )
                )

                // Auto-focus and open keyboard when TextField appears
                LaunchedEffect(showTextField) {
                    if (showTextField) {
                        delay(100) // small delay to ensure layout
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                }
            }
        }
    }
}

@Composable
fun Trackpad(onEvent: (JSONObject) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    var dragOccurred = false

                    while (true) {
                        val event = awaitPointerEvent()
                        val pointers = event.changes.size

                        if (pointers == 1) {
                            val change = event.changes[0]
                            val delta = change.positionChange()

                            if (delta != Offset.Zero) {
                                // Single-finger drag → mouse move
                                val json = JSONObject().apply {
                                    put("action", MouseActionType.MOVE)
                                    put("dx", delta.x.roundToInt())
                                    put("dy", delta.y.roundToInt())
                                }
                                onEvent(json)
                                change.consume()
                                dragOccurred = true
                            }

                            if (change.changedToUp()) {
                                if (!dragOccurred) {
                                    // Single tap → left click
                                    val json =
                                        JSONObject().apply { put("action", MouseActionType.CLICK) }
                                    onEvent(json)
                                }
                                break
                            }

                        } else if (pointers == 2) {
                            val changes = event.changes.toList()

                            val dy0 = changes[0].positionChange().y
                            val dy1 = changes[1].positionChange().y
                            val dx0 = changes[0].positionChange().x
                            val dx1 = changes[1].positionChange().x

                            // Only consider vertical movement if vertical is greater than horizontal
                            val verticalMove = (abs(dy0) > abs(dx0) || abs(dy1) > abs(dx1))
                            if (verticalMove && (abs(dy0) > 0.5f || abs(dy1) > 0.5f)) {
                                val amount = -((dy0 + dy1) / 2).roundToInt()
                                val json = JSONObject().apply {
                                    put("action", MouseActionType.SCROLL)
                                    put("amount", amount)
                                }
                                onEvent(json)
                                event.changes.forEach { it.consume() }
                            }
                        }
                    }
                }
            }
    )
}

// Replace this with your actual brush

