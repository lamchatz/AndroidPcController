package lchat.pccontroller

import android.app.Application
import lchat.pccontroller.data.AppDatabase
import lchat.pccontroller.data.repo.PcRepository

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MouseWebSocketClient.init(PcRepository.getInstance(AppDatabase.getDatabase(this).pcRepo()), this)
    }
}