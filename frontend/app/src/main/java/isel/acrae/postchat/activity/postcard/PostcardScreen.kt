package isel.acrae.postchat.activity.postcard

import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import isel.acrae.postchat.activity.postcard.draw.Dimensions
import isel.acrae.postchat.ui.composable.PostChatTopAppBar
import isel.acrae.postchat.utils.getSvgDimensions
import isel.acrae.postchat.utils.zoomPanOrDrag


@Composable
fun PostCardScreen(
    path: String
) {
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    val templatePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .decoderFactory(SvgDecoder.Factory())
            .data(path).build()
    )
    val svgDimensions = getSvgDimensions(path)
    val scaledCanvas = (screenWidth.toFloat() / svgDimensions.width.hashCode()) - 0.1f
    val scale = remember { mutableStateOf(scaledCanvas) }
    val pan = remember { mutableStateOf(Offset.Zero) }

    val dimensions = Dimensions(
        svgDimensions.width.hashCode(),
        svgDimensions.height.hashCode(),
        scaledDensity
    )
    Scaffold(
        topBar = { PostChatTopAppBar() },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(top = 5.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null
                )
            }
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.secondaryContainer),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier
                    .weight(1f)
                    .graphicsLayer {
                        translationY = pan.value.y * scale.value
                        translationX = pan.value.x * scale.value
                        scaleX = scale.value
                        scaleY = scale.value
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier
                        .width(dimensions.widthDp)
                        .height(dimensions.heightDp)
                        .border(1.dp, MaterialTheme.colorScheme.outline)
                        .clipToBounds(),
                ) {
                    ViewCanvas(
                        templatePainter = templatePainter,
                        onZoomOrPan = { zoom, _pan ->
                            val aux = scale.value * zoom
                            if (aux >= scaledCanvas - 0.5f)
                                scale.value *= zoom
                            pan.value += _pan
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun ViewCanvas(
    templatePainter: AsyncImagePainter,
    onZoomOrPan: (zoom: Float, pan: Offset) -> Unit,
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    zoomPanOrDrag(
                        onZoomOrPan = onZoomOrPan,
                        onDragStart = {},
                        onDrag = {},
                        onDragEnd = {}
                    )
                }
            }

    ) {
        with(templatePainter) { draw(size) }
    }
}

