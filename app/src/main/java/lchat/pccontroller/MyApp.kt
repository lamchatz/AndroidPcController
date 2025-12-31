package lchat.pccontroller

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MouseWebSocketClient.connect("ws://${RequestHandler.host}/mouseKeyboard")

    }
}