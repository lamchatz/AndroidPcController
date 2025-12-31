package lchat.pccontroller.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import lchat.pccontroller.RequestHandler
import lchat.pccontroller.data.ConnectionTestResult
import lchat.pccontroller.data.PC

@Composable
fun EditConnectionDialog(
    show: Boolean,
    connectionToEdit: PC?,
    onSave: (ip: String, port: String, nickname: String) -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    if (!show) return

    val coroutineScope = rememberCoroutineScope()
    var nickname by remember { mutableStateOf(connectionToEdit?.nickName.orEmpty()) }
    var ip by remember { mutableStateOf(connectionToEdit?.ip.orEmpty()) }
    var port by remember { mutableStateOf(connectionToEdit?.port.orEmpty()) }

    // Local state for test results
    var testInProgress by remember { mutableStateOf(false) }
    var connectionTestResult by remember { mutableStateOf<ConnectionTestResult?>(null) }

    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(12.dp)
                    .widthIn(min = 280.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Connection Settings",
                    style = MaterialTheme.typography.titleMedium
                )

                OutlinedTextField(
                    value = ip,
                    onValueChange = { ip = it },
                    label = { Text("IP Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = port,
                    onValueChange = { port = it },
                    label = { Text("Port") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        coroutineScope.launch {
                            testInProgress = true
                            connectionTestResult = RequestHandler.testConnection(ip, port)
                            testInProgress = false
                        }
                    },
                    enabled = ip.isNotBlank() && port.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1866A2),
                        contentColor = Color.White
                    )
                ) {
                    if (testInProgress) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Test Connection")
                    }
                }

                connectionTestResult?.let { result ->
                    val (message, color) = when (result) {
                        ConnectionTestResult.Success -> "Connection Successful!" to Color(0xFF30B233)
                        ConnectionTestResult.Timeout -> "Connection timed out" to Color(0xFFE67E22)
                        is ConnectionTestResult.Error -> "Error: ${result.message}" to Color.Red
                    }
                    Text(message, color = color)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    if (connectionToEdit != null) {
                        Button(
                            onClick = onDelete,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Delete")
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Button(
                        onClick = {
                            onSave(ip, port, nickname)
                        },
                        enabled = ip.isNotBlank() && port.isNotBlank() && nickname.isNotBlank()
                    ) {
                        Text("Save & Select", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}
