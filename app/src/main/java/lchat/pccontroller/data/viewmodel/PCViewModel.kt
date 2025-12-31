package lchat.pccontroller.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import lchat.pccontroller.data.AppDatabase
import lchat.pccontroller.data.PC

class PCViewModel(application: Application) : AndroidViewModel(application) {
    private val pcRepo = AppDatabase.getDatabase(application).pcRepo()

    private val _Pcs = MutableStateFlow<List<PC>>(emptyList())
    val pcs: StateFlow<List<PC>> = _Pcs

    init {
        viewModelScope.launch {
            _Pcs.value = pcRepo.getAll()
        }
    }

    fun addPc(pc: PC) {
        viewModelScope.launch {
            if (pc.selected) {
                pcRepo.deselect()
            }
            pcRepo.insert(pc)
            _Pcs.value = pcRepo.getAll()
        }
    }

    fun delete(pc: PC) {
        viewModelScope.launch {
            pcRepo.deleteByIp(pc.ip)
            _Pcs.value = pcRepo.getAll()
        }
    }

    fun update(pc: PC) {
        viewModelScope.launch {
            if (pc.selected) {
                pcRepo.deselect()
            }
            pcRepo.update(pc)
            _Pcs.value = pcRepo.getAll()
        }
    }

}