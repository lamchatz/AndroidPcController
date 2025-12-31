package lchat.pccontroller.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import lchat.pccontroller.MouseWebSocketClient
import lchat.pccontroller.R
import lchat.pccontroller.components.utils.MenuGradient
import org.json.JSONObject
import kotlin.math.abs
import kotlin.math.roundToInt

enum class ActionType {
    CLICK,
    MOVE,
    SCROLL,
    TYPE,
    BACKSPACE,
    ENTER,
    ESC
}

private val SIZE = 64.dp

@Composable
fun TrackpadScreen() {
    var showTextField by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var previousText by remember { mutableStateOf("") }

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

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 56.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                SmallFloatingActionButton(
                    onClick = {
                        val json =
                            JSONObject().apply { put("action", ActionType.ESC) }
                        MouseWebSocketClient.sendAction(json)
                    },
                    modifier = Modifier.size(SIZE),
                    shape = CircleShape,
                    containerColor = Color(0xFF6FD573),
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.esc),
                        contentDescription = "Escape"
                    )
                }

                SmallFloatingActionButton(
                    onClick = {
                        val json =
                            JSONObject().apply { put("action", ActionType.ENTER) }
                        MouseWebSocketClient.sendAction(json)
                    },
                    modifier = Modifier.size(SIZE),
                    shape = CircleShape,
                    containerColor = Color(0xFF6FD573),
                    contentColor = Color.DarkGray,
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.enter),
                        contentDescription = "Enter"
                    )
                }

                // Bottom button (your original one)
                SmallFloatingActionButton(
                    onClick = { showTextField = true },
                    modifier = Modifier.size(SIZE),
                    shape = CircleShape,
                    containerColor = Color(0xFF6FD573),
                    contentColor = Color.DarkGray,
                    elevation = FloatingActionButtonDefaults.elevation(4.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.space),
                        contentDescription = "Text Input"
                    )
                }
            }
        }

        if (showTextField) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x88000000))
                    .clickable {
                        showTextField = false
                        focusManager.clearFocus()
                        text = ""
                        previousText = ""
                    },
                contentAlignment = Alignment.Center
            ) {
                TextField(
                    value = text,
                    onValueChange = { newText ->
                        val diff = newText.length - previousText.length

                        if (diff > 0) {
                            val typedChar = newText.substring(previousText.length)
                            typedChar.forEach { c ->
                                val json = JSONObject().apply {
                                    put("action", ActionType.TYPE)
                                    put("text", c.toString())
                                }
                                MouseWebSocketClient.sendAction(json)
                            }
                        } else if (diff < 0) {
                            repeat(-diff) {
                                val json = JSONObject().apply {
                                    put("action", ActionType.BACKSPACE)
                                }
                                MouseWebSocketClient.sendAction(json)
                            }
                        }

                        previousText = newText
                        text = newText
                    },
                    placeholder = { Text("Type here") },
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .focusRequester(focusRequester)
                )

                LaunchedEffect(showTextField) {
                    if (showTextField) {
                        delay(100)
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
                                    put("action", ActionType.MOVE)
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
                                        JSONObject().apply { put("action", ActionType.CLICK) }
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

                            val verticalMove = (abs(dy0) > abs(dx0) || abs(dy1) > abs(dx1))
                            if (verticalMove && (abs(dy0) > 0.5f || abs(dy1) > 0.5f)) {
                                val amount = -((dy0 + dy1) / 2).roundToInt()
                                val json = JSONObject().apply {
                                    put("action", ActionType.SCROLL)
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