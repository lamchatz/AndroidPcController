package lchat.pccontroller.components.buttons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lchat.pccontroller.R

@Composable
fun PopupItemWithIcon(
    text: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    height: Dp = 100.dp,
    iconHeight: Dp = 4.dp,
    textColor: Color = Color.White,
    iconSize: Dp = 40.dp,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(height)
            .clickable(onClick = onClick)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = "$text icon",
                tint = Color.Black,
                modifier = Modifier.size(iconSize)
            )
            Spacer(modifier = Modifier.height(iconHeight))
            Text(
                text = text,
                color = textColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PopupItemWithIconPreview() {
    // Optional: wrap in your theme if needed
    MaterialTheme {
        PopupItemWithIcon(
            text = "Spotify",
            icon = painterResource(id = R.drawable.spotify), // make sure you have this logo in drawable
            onClick = {
                // For preview, we don't actually launch Spotify, just return true

            }
        )
    }
}