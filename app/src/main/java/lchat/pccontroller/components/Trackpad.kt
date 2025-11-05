package lchat.pccontroller.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import org.json.JSONObject
import kotlin.math.abs
import kotlin.math.roundToInt

enum class MouseActionType {
    CLICK,
    MOVE,
    SCROLL,
    RIGHT_CLICK
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

                            // Two-finger tap → right click

                            if (changes.all { it.changedToUp() }) {
                                val moved = changes.any { it.positionChange() != Offset.Zero }
                                if (!moved) {
                                    val json = JSONObject().apply {
                                        put(
                                            "action",
                                            MouseActionType.RIGHT_CLICK
                                        )
                                    }
                                    onEvent(json)
                                }
                                break
                            }
                        }
                    }
                }
            }
    )
}
