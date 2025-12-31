package lchat.pccontroller.data.repo

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import lchat.pccontroller.data.PC

class PcRepository private constructor(
    private val dao: PCRepo
) {
    val DEFAULT_BASE_URL = "-1.-1.-1.-1"

    private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _selectedPc = MutableStateFlow<PC?>(PC(-1,"-1.-1.-1.-1", "-1", "invalid", false))
    val selectedPc: StateFlow<PC?> = _selectedPc

    private val _baseUrl = MutableStateFlow(DEFAULT_BASE_URL)
    val baseUrl: StateFlow<String> = _baseUrl


    init {
        // Collect Room Flow and update StateFlow
        repoScope.launch {
            dao.observeSelectedPC().collect { pc ->
                if (pc != null && pc.id >= 0) {
                    _selectedPc.value = pc
                    _baseUrl.value = pc.let { "http://${it.ip}:${it.port}/" }
                } else {
                    _baseUrl.value = DEFAULT_BASE_URL
                }
            }
        }
    }

    suspend fun getAll(): List<PC> {
        return dao.getAll()
    }


    companion object {
        @Volatile
        private var INSTANCE: PcRepository? = null

        fun getInstance(dao: PCRepo): PcRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PcRepository(dao).also {
                    INSTANCE = it
                }
            }
        }
    }
}