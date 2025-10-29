package lchat.pccontroller

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import lchat.pccontroller.components.AgeButton
import lchat.pccontroller.components.DisneyButton
import lchat.pccontroller.components.SpotifyButton
import lchat.pccontroller.components.YoutubeButton

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }

    }

    @Composable
    fun MyApp() {
        MaterialTheme {
            // Define your gradient
            val gradient = Brush.linearGradient(
                colors = listOf(
                    Color(color = 0xFF211F2F),
                    Color(0xFF918CA9)
                ),
                start = Offset(0f, 0f),
                end = Offset(Float.POSITIVE_INFINITY, 0f) // left → right
            )

            // Apply it to the Box background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = gradient),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    SpotifyButton()
                    AgeButton()
                    YoutubeButton()
                    DisneyButton()
                }
            }
        }
    }
}
