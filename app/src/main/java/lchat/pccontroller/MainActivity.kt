package lchat.pccontroller

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import lchat.pccontroller.components.AppNavHost

class MainActivity : ComponentActivity() {

    private val volumeViewModel: VolumeViewModel by viewModels()

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {

        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {

                KeyEvent.KEYCODE_VOLUME_UP -> {
                    lifecycleScope.launch { volumeViewModel.onVolumeUp() }
                    return true // block system volume change
                }

                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    lifecycleScope.launch { volumeViewModel.onVolumeDown() }
                    return true // block system volume change
                }
            }
        }

        return super.dispatchKeyEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface {
                    AppNavHost(volumeViewModel)
                }
            }
        }
    }
}