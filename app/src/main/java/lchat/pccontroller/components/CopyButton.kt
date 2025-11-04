package lchat.pccontroller.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lchat.pccontroller.ClipboardUtils
import lchat.pccontroller.RequestHandler
import lchat.pccontroller.RequestHandler.Companion.executeRequest

@Composable
fun CopyButton() {
    var showDialog by remember { mutableStateOf(false) }
    var animateDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val close = {
        animateDialog = false
        coroutineScope.launch {
            delay(POPUP_ANIMATION_DURATION.toLong())
            showDialog = false
        }
    }

    BaseButton(
        text = "Copy",
        msg = "Copied to PC!",
        longPressMsg = "Copied from PC!",
        containerColor = Color(0xFF6343A2),
        onClick = {
            RequestHandler.paste(ClipboardUtils.getText(context))
        },
        onDoubleClick = {
            showDialog = true
            true
        },
        onLongPress = {
            val output = RequestHandler.copy()

            if (output != "error" && output != "empty") {
                ClipboardUtils.copy(context, output)
                true
            } else {
                false
            }
        }
    )

    if (showDialog) {
        LaunchedEffect(Unit) {
            animateDialog = true
        }

        CopyPopup(
            visible = animateDialog,
            onDismiss = {
                close()
            },
            onSendClick = { text ->
                executeRequest(
                    context = context,
                    scope = coroutineScope,
                    request = { RequestHandler.paste(text) },
                    successMessage = "Copied to PC!"
                )
                close()
            }
        )
    }
}


@Composable
fun CopyPopup(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSendClick: (text: String) -> Unit
) {

    var inputText by remember { mutableStateOf("") }
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
                        brush = CopyMenu,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Enter text to copy:", fontWeight = FontWeight.Bold)

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    singleLine = true,
                    placeholder = { Text("Type something...") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onSendClick(inputText) // trigger Send on Enter/Done
                        }
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onSendClick(inputText)
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun CopyPopup2(
    visible: Boolean,
    onDismiss: () -> Unit,
    onSendClick: (text: String) -> Unit
) {
    if (!visible) return

    var inputText by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Enter text to copy:", fontWeight = FontWeight.Bold)

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    singleLine = true,
                    placeholder = { Text("Type something...") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onSendClick(inputText) // trigger Send on Enter/Done
                        }
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            onSendClick(inputText)
                        }
                    ) {
                        Text("Send")
                    }
                }
            }
        }
    }
}