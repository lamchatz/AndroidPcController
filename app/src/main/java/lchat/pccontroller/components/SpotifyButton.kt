package lchat.pccontroller.components

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler

@Composable
fun SpotifyButton() {
    BaseButton(
        text = "Spotify",
        msg = "Opening Spotify",
        containerColor = Color(0xFF1DB954),
        icon = painterResource(id = R.drawable.spotify),
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
        }
    )
}
