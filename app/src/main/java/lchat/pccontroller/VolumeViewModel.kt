package lchat.pccontroller

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

class VolumeViewModel : ViewModel() {

    private val _volumeEvent = MutableSharedFlow<VolumeKey>()
    val volumeEvent = _volumeEvent

    suspend fun onVolumeUp() {
        _volumeEvent.emit(VolumeKey.Up)
    }

    suspend fun onVolumeDown() {
        _volumeEvent.emit(VolumeKey.Down)
    }
}

enum class VolumeKey { Up, Down }