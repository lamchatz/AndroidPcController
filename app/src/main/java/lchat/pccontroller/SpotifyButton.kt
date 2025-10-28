package lchat.pccontroller

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SpotifyButton() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                // Call your suspend function
                val success = RequestHandler.openSpotify()

                val text = if (success) "Opening Spotify!" else "Something went wrong"
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()

                if (success) {
                    // Try to open Spotify app
                    val pm: PackageManager = context.packageManager
                    val launchIntent: Intent? = pm.getLaunchIntentForPackage("com.spotify.music")
                    if (launchIntent != null) {
                        context.startActivity(launchIntent)
                    } else {
                        // Spotify not installed, open web as fallback
                        val uri = Uri.parse("https://open.spotify.com/")
                        val webIntent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(webIntent)
                    }
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .width(200.dp)
            .height(60.dp)
    ) {
        Text("Open Spotify")
    }
}
