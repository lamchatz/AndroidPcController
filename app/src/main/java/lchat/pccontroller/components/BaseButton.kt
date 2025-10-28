package lchat.pccontroller.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun BaseButton(
    text: String,
    msg: String,
    containerColor: Color,
    textColor: Color = Color.White,
    icon: Painter? = null,
    width: Dp = 250.dp,
    height: Dp = 60.dp,
    shape: Shape = RoundedCornerShape(16.dp),
    onClick: suspend (context: Context) -> Boolean
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                val success = onClick(context)
                val toastText = if (success) msg else "Something went wrong"
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            }
        },
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        modifier = Modifier
            .width(width)
            .height(height)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            if (icon != null) {
                Icon(
                    painter = icon,
                    contentDescription = "$text logo",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(72.dp)        // bigger icon
                        .padding(end = 16.dp) // more spacing
                )
            }
            Text(
                text = text,
                color = textColor,
                fontSize = 20.sp,       // slightly bigger text
                fontWeight = FontWeight.Bold
            )
        }
    }


}
