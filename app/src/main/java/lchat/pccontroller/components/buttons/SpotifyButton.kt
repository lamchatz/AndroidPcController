package lchat.pccontroller.components.buttons

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun SpotifyButton() {
    BaseIconButton(
        icon = painterResource(id = R.drawable.spotify),
        containerColor = Color(0xFF1DB954),
        onClick = { context ->
            val success = RequestHandler.openSpotify()
            if (success) {
                val pm: PackageManager = context.packageManager
                val launchIntent: Intent? = pm.getLaunchIntentForPackage("com.spotify.music")
                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                } else {
                    val uri = Uri.parse("https://open.spotify.com/")
                    val webIntent = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(webIntent)
                }
            }
            success
        },
        successMessage = "Opening Spotify"
    )
}

@Composable
@Preview
fun PreviewSpotifyButton() {
    SpotifyButton()
}
