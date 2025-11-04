package lchat.pccontroller.components

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import lchat.pccontroller.R
import lchat.pccontroller.RequestHandler
import lchat.pccontroller.RequestHandler.Companion.executeRequest
import lchat.pccontroller.yt.YouTubeViewModel


@Composable
fun YoutubeButton(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var animateDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val close = {
        animateDialog = false
        coroutineScope.launch {
            delay(POPUP_ANIMATION_DURATION.toLong())
            showDialog = false
        }
    }

    BaseButton(
        text = "Youtube",
        icon = painterResource(id = R.drawable.youtube),
        containerColor = Color(0xFFFF0000),
        onClick = {
            showDialog = true
            true
        }
    )

    if (showDialog) {
        LaunchedEffect(Unit) {
            animateDialog = true
        }

        YoutubeOptionsDialog(
            visible = animateDialog,
            onDismiss = {
                close()
            },
            onHomeClicked = {
                executeRequest(
                    context = context,
                    scope = coroutineScope,
                    request = { RequestHandler.openYoutube() },
                    successMessage = "Opening Youtube!"
                )
                close()
            },
            onSearchClicked = {
                close()
                navController.navigate("youtube_search")
            }
        )
    }
}

@Composable
fun YoutubeOptionsDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onHomeClicked: () -> Unit,
    onSearchClicked: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {
        AnimatedVisibility(
            visible = visible,
            enter = DialogEnterAnimation,
            exit = DialogExitAnimation
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        brush = YoutubeMenu,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Youtube",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PopupItemWithIcon(
                        text = "Home",
                        icon = painterResource(id = R.drawable.home),
                        onClick = onHomeClicked,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    PopupItemWithIcon(
                        text = "Search",
                        icon = painterResource(id = R.drawable.search),
                        onClick = onSearchClicked,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun YoutubeSearchScreen(viewModel: YouTubeViewModel = viewModel()) {
    val videos by viewModel.videos.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var hasSearched by remember { mutableStateOf(false) }

    // Animate top padding for search field
    val topPadding by animateDpAsState(targetValue = if (hasSearched) 32.dp else 400.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = YoutubeSearch)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(topPadding))

        // Search field
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                focusedLabelColor = Color.White
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (query.isNotBlank()) {
                        hasSearched = true
                        viewModel.searchVideos(query)
                        val imm =
                            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(
                            (context as Activity).currentFocus?.windowToken,
                            0
                        )
                    }
                }
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Videos list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()

        ) {
            items(videos) { video ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            val videoId = video.id.videoId
                            executeRequest(
                                context = context,
                                scope = scope,
                                request = { RequestHandler.openYoutubeVideo(videoId) },
                                successMessage = "Opening video...",
                                errorMessage = "Failed to open video"
                            )
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {

                    Column(modifier = Modifier.padding(8.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(video.snippet.thumbnails.medium.url),
                            contentDescription = video.snippet.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Text(video.snippet.title, style = MaterialTheme.typography.titleMedium)
                        Text(video.snippet.description, maxLines = 2)
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun YoutubeSearchScreenPreview(viewModel: YouTubeViewModel = viewModel()) {
    YoutubeSearchScreen()
}