package lchat.pccontroller.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import lchat.pccontroller.R
import lchat.pccontroller.data.PC
import lchat.pccontroller.data.viewmodel.PCViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LongBasicDropdownMenu(pcViewModel: PCViewModel = viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    val options by pcViewModel.pcs.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var connectionToEdit by remember { mutableStateOf<PC?>(null) }

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                painter = painterResource(R.drawable.setting),
                contentDescription = "Manage connections"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.nickName) },
                    trailingIcon = {
                        if (option.selected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    onClick = {
                        expanded = false
                        showDialog = true
                        connectionToEdit = option
                    }
                )
            }

            if (options.isNotEmpty()) {
                HorizontalDivider()
            }

            // Third section
            DropdownMenuItem(
                text = { Text("Add Connection") },
                leadingIcon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                onClick = {
                    expanded = false
                    showDialog = true
                    connectionToEdit = null
                }
            )
        }

        EditConnectionDialog(
            show = showDialog,
            connectionToEdit = connectionToEdit,
            onSave = { ip, port, nickname ->
                showDialog = false
                println("IP: $ip, Nickname: $nickname")
                if (connectionToEdit == null) {
                    pcViewModel.addPc(PC(0, ip, port, nickname, true))
                } else {
                    pcViewModel.update(
                        connectionToEdit!!.copy(
                            ip = ip,
                            port = port,
                            nickName = nickname,
                            selected = true
                        )
                    )
                }
            },
            onCancel = {
                showDialog = false
            },
            onDelete = {
                showDialog = false
                pcViewModel.delete(connectionToEdit!!)
            }
        )
    }
}