package lchat.pccontroller

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import lchat.pccontroller.components.AppNavHost
import lchat.pccontroller.data.AppDatabase
import lchat.pccontroller.data.ConnectionTestResult
import lchat.pccontroller.data.repo.PcRepository

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


        val db = AppDatabase.getDatabase(this)
        val repository = PcRepository.getInstance(db.pcRepo())

        lifecycleScope.launch {
            val allPcs = repository.getAll()
            if (allPcs.isEmpty()) {
                Toast.makeText(this@MainActivity, "No Connection available", Toast.LENGTH_SHORT)
                    .show()
            } else {
                repository.selectedPc.collect { pc ->
                    if (pc == null) {
                        Toast.makeText(
                            this@MainActivity,
                            "No Connection selected",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        when (RequestHandler.testConnection(pc.ip, pc.port)) {
                            is ConnectionTestResult.Error -> Toast.makeText(
                                this@MainActivity,
                                "Selected Connection has Errors",
                                Toast.LENGTH_SHORT
                            ).show()

                            ConnectionTestResult.Timeout -> Toast.makeText(
                                this@MainActivity,
                                "Selected Connection Timed out",
                                Toast.LENGTH_SHORT
                            ).show()

                            ConnectionTestResult.Success -> {
                                //do Nothing
                            }
                        }
                    }
                }
            }
        }

        setContent {
            MaterialTheme {

                Surface {
                    AppNavHost(volumeViewModel)
                }
            }
        }
    }
}