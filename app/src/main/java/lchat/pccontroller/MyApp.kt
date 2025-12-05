package lchat.pccontroller

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MouseWebSocketClient.connect("ws://192.168.1.11:9091/mouseKeyboard")

    }
}