package lchat.pccontroller

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MouseWebSocketClient.connect("ws://192.168.2.7:9091/mouse")

    }
}