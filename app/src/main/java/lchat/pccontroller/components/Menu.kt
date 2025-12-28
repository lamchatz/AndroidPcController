package lchat.pccontroller.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import lchat.pccontroller.R
import lchat.pccontroller.VolumeViewModel
import lchat.pccontroller.components.buttons.AgeButton
import lchat.pccontroller.components.buttons.AndroidStudioButton
import lchat.pccontroller.components.buttons.CloseButton
import lchat.pccontroller.components.buttons.CopyButton
import lchat.pccontroller.components.buttons.IntellijButton
import lchat.pccontroller.components.buttons.NetflixButton
import lchat.pccontroller.components.buttons.OperaButton
import lchat.pccontroller.components.buttons.SpotifyButton
import lchat.pccontroller.components.buttons.TODOButton
import lchat.pccontroller.components.utils.MenuGradient
import lchat.pccontroller.data.viewmodel.PCViewModel
import lchat.pccontroller.yt.YoutubeButton

private const val PC_PREFERENCES_NAME = "pc_preferences"

private val Context.dataStore by preferencesDataStore(
    name = PC_PREFERENCES_NAME
)

@Composable
fun MenuScreen(
    navController: NavController,
    volumeViewModel: VolumeViewModel
) {

    val buttons: List<@Composable () -> Unit> = listOf(
        { SpotifyButton() },
        { AgeButton() },
        { YoutubeButton(navController) },
        //{ DisneyButton() },
        { NetflixButton() },
        { CopyButton() },
        { CloseButton() },
        { IntellijButton() },
        { AndroidStudioButton() },
        { OperaButton() },
        { TODOButton() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = MenuGradient)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 20) {
                        if (navController.currentDestination?.route != "trackpad") {
                            navController.navigate("trackpad") {
                                launchSingleTop = true
                            }
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            LongBasicDropdownMenu()
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(horizontal = 36.dp)
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            items(buttons.size) { index ->
                buttons[index]()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            VolumeUtils(volumeViewModel)
        }
    }
}

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
                    text = { Text("${option.ip} - ${option.nickName}") },
                    onClick = {
                        expanded = false

                    }
                )
            }

            HorizontalDivider()

            // Third section
            DropdownMenuItem(
                text = { Text("Add Connection") },
                leadingIcon = { Icon(Icons.Outlined.Add, contentDescription = null) },
                onClick = { /* Do something... */ }
            )
        }


    }
}