package lchat.pccontroller.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import lchat.pccontroller.data.viewmodel.PCViewModel


@Composable
fun LongBasicDropdownMenu(pcViewModel: PCViewModel = viewModel()) {
    var expanded by remember { mutableStateOf(false) }
    val options by pcViewModel.pcs.collectAsState()

    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(painter = painterResource(R.drawable.setting), contentDescription = "Connect to ip")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.nickName) },
                    onClick = {
                        expanded = false

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
                onClick = { /* Do something... */ }
            )
        }


    }
}