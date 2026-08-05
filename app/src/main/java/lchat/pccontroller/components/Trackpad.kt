package lchat.pccontroller.components
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lchat.pccontroller.MouseWebSocketClient
import lchat.pccontroller.R
import lchat.pccontroller.components.utils.MenuGradient
import org.json.JSONObject
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

enum class ActionType {
    CLICK,
    RIGHT_CLICK,
    MOVE,
    SCROLL,
    TYPE,
    BACKSPACE,
    ENTER,
    ESC,
    DESKTOP
}

private val FAB_SIZE = 60.dp
private val OPTION_SIZE = 48.dp
private const val SCROLL_STEP = 8

private val ControlGreen = Color(0xFF6FD573)
private val DarkIconColor = Color(0xFF303030)

@Composable
fun TrackpadScreen() {
    var showTextField by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("") }
    var previousText by remember { mutableStateOf("") }
    var isMenuExpanded by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val scrollButtonStyle = Modifier
        .size(FAB_SIZE)
        .shadow(4.dp, CircleShape)
        .clip(CircleShape)
        .background(ControlGreen)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MenuGradient)
    ) {
        // Trackpad area with Long-Press Right-Click support
        Trackpad { event ->
            MouseWebSocketClient.sendAction(event)
        }

        // --- SCROLL BUTTONS (Middle Right) ---
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionButton(
                iconId = R.drawable.up,
                contentDescription = "Scroll Up",
                modifier = scrollButtonStyle.repeatingScroll(-SCROLL_STEP)
            )

            ActionButton(
                iconId = R.drawable.down,
                contentDescription = "Scroll Down",
                modifier = scrollButtonStyle.repeatingScroll(SCROLL_STEP)
            )
        }

        // --- EXPANDING RADIAL SPEED DIAL MENU (Bottom Right) ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 56.dp, end = 24.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            // Arc Options Container
            AnimatedVisibility(
                visible = isMenuExpanded,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Box(
                    modifier = Modifier.size(200.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    // 1. ESC Button
                    MenuArcOption(
                        angleDeg = 180f, // Leftmost
                        onClick = {
                            isMenuExpanded = false
                            sendAction(ActionType.ESC)
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(DarkIconColor, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("ESC", color = ControlGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // 2. Show Desktop Button (Win + D)
                    MenuArcOption(
                        angleDeg = 150f,
                        onClick = {
                            isMenuExpanded = false
                            sendAction(ActionType.DESKTOP)
                        }
                    ) {
                        CircleOptionIcon(iconId = R.drawable.desktop, contentDescription = "Desktop")
                    }

                    // 3. ENTER Button
                    MenuArcOption(
                        angleDeg = 120f,
                        onClick = {
                            isMenuExpanded = false
                            sendAction(ActionType.ENTER)
                        }
                    ) {
                        CircleOptionIcon(iconId = R.drawable.enter, contentDescription = "Enter")
                    }

                    // 4. TYPE/Keyboard Button
                    MenuArcOption(
                        angleDeg = 90f, // Topmost
                        onClick = {
                            isMenuExpanded = false
                            showTextField = true
                        }
                    ) {
                        CircleOptionIcon(iconId = R.drawable.space, contentDescription = "Text Input")
                    }
                }
            }

            // Main Trigger FAB Toggle Button
            val rotationAngle by animateFloatAsState(
                targetValue = if (isMenuExpanded) 45f else 0f,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "fabRotation"
            )

            Box(
                modifier = Modifier
                    .size(FAB_SIZE)
                    .shadow(6.dp, CircleShape)
                    .clip(CircleShape)
                    .background(ControlGreen)
                    .clickable { isMenuExpanded = !isMenuExpanded },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.widget),
                    contentDescription = "Toggle Actions Menu",
                    tint = DarkIconColor,
                    modifier = Modifier
                        .size(26.dp)
                        .rotate(rotationAngle)
                )
            }
        }

        // Fullscreen Text Field Overlay
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

// Trackpad with move, quick tap (left click), and long press (right click)
@Composable
fun Trackpad(onEvent: (JSONObject) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    var dragOccurred = false
                    val longPressThreshold = 500L
                    val moveTolerance = 10f
                    var initialPosition = Offset.Zero

                    val down = awaitPointerEvent().changes.firstOrNull() ?: return@awaitEachGesture
                    initialPosition = down.position
                    val downTime = System.currentTimeMillis()

                    while (true) {
                        val event = awaitPointerEvent()
                        val pointers = event.changes.size

                        if (pointers == 1) {
                            val change = event.changes[0]
                            val delta = change.positionChange()

                            if ((change.position - initialPosition).getDistance() > moveTolerance) {
                                dragOccurred = true
                            }

                            if (delta != Offset.Zero) {
                                val json = JSONObject().apply {
                                    put("action", ActionType.MOVE)
                                    put("dx", delta.x.roundToInt())
                                    put("dy", delta.y.roundToInt())
                                }
                                onEvent(json)
                                change.consume()
                            }

                            if (change.changedToUp()) {
                                if (!dragOccurred) {
                                    val pressDuration = System.currentTimeMillis() - downTime
                                    val action = if (pressDuration >= longPressThreshold) {
                                        ActionType.RIGHT_CLICK
                                    } else {
                                        ActionType.CLICK
                                    }
                                    val json = JSONObject().apply { put("action", action) }
                                    onEvent(json)
                                }
                                break
                            }
                        } else {
                            break
                        }
                    }
                }
            }
    )
}

@Composable
private fun MenuArcOption(
    radiusDp: Float = 130f,
    angleDeg: Float,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val angleRad = Math.toRadians(angleDeg.toDouble())
    val xOffset = (radiusDp * cos(angleRad)).dp
    val yOffset = (-radiusDp * sin(angleRad)).dp

    Box(
        modifier = Modifier
            .offset(x = xOffset, y = yOffset)
            .size(OPTION_SIZE)
            .shadow(4.dp, CircleShape)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun CircleOptionIcon(iconId: Int, contentDescription: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ControlGreen),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription,
            tint = DarkIconColor,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun ActionButton(
    iconId: Int,
    contentDescription: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = contentDescription,
            tint = DarkIconColor,
            modifier = Modifier.size(28.dp)
        )
    }
}

private fun sendAction(actionType: ActionType) {
    val json = JSONObject().apply { put("action", actionType) }
    MouseWebSocketClient.sendAction(json)
}

private fun Modifier.repeatingScroll(
    scrollAmount: Int,
    initialDelayMillis: Long = 300L,
    repeatDelayMillis: Long = 60L
): Modifier = composed {
    val coroutineScope = rememberCoroutineScope()
    var scrollJob by remember { mutableStateOf<Job?>(null) }

    fun emitScroll() {
        val json = JSONObject().apply {
            put("action", ActionType.SCROLL)
            put("amount", scrollAmount)
        }
        MouseWebSocketClient.sendAction(json)
    }

    pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                emitScroll()
                scrollJob = coroutineScope.launch {
                    delay(initialDelayMillis)
                    while (true) {
                        emitScroll()
                        delay(repeatDelayMillis)
                    }
                }
                try {
                    awaitRelease()
                } finally {
                    scrollJob?.cancel()
                }
            }
        )
    }
}