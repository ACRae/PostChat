package isel.acrae.postchat.activity.postcard

import android.content.res.Resources
import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.caverock.androidsvg.SVG
import isel.acrae.postchat.activity.postcard.draw.Dimensions
import isel.acrae.postchat.ui.composable.ExpandableFAB
import isel.acrae.postchat.ui.composable.PopDialog
import isel.acrae.postchat.ui.composable.PostChatTopAppBar
import isel.acrae.postchat.ui.composable.SmallExpandableFABItem
import isel.acrae.postchat.ui.composable.showToast
import isel.acrae.postchat.utils.Handle
import isel.acrae.postchat.utils.getSvgDimensions
import isel.acrae.postchat.utils.zoomPanOrDrag
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


@Composable
fun PostCardScreen(
    isSent: Boolean,
    path: String,
    imagePath: String,
    htrText: String,
    htr: () -> Unit,
) {
    val context = LocalContext.current
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    val templatePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .decoderFactory(SvgDecoder.Factory())
            .data(path).build()
    )
    val svgDimensions = getSvgDimensions(path)
    val scaledCanvas = (screenWidth.toFloat() / svgDimensions.width.hashCode()) - 0.1f
    val scale = remember { mutableStateOf(scaledCanvas) }
    val pan = remember { mutableStateOf(Offset.Zero) }

    var popSavePng by remember { mutableStateOf(false) }
    var popHTR by remember { mutableStateOf(false) }
    var quality by remember { mutableStateOf(1.0F) }

    val dimensions = Dimensions(
        svgDimensions.width.hashCode(),
        svgDimensions.height.hashCode(),
        scaledDensity
    )
    Scaffold(
        topBar = { PostChatTopAppBar() },
        floatingActionButton = {
            ExpandableFAB(description = "Settings", icon = Icons.Default.Settings) {
                SmallExpandableFABItem(description = "Save postcard", icon = Icons.Default.Save) { popSavePng = true}
                SmallExpandableFABItem(description = "HTR", icon = Icons.Default.Translate) { popHTR = true }
            }
        }
    ) { padding ->
        if(popSavePng) {
            PopDialog(
                onConfirm = {
                    convertSvgToPng(imagePath, path, quality.toInt())
                    popSavePng = false
                    showToast(context, "Image Saved")
                },
                onDismiss = { popSavePng = false }
            ){
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Quality = ${quality.toInt()}")
                    Spacer(modifier = Modifier.height(8.dp))

                    Slider(
                        value = quality,
                        onValueChange = { newValue ->
                            quality = newValue
                        },
                        modifier = Modifier.fillMaxWidth(),
                        steps = 3,
                        valueRange = 1.0F..5.0F
                    )
                }
            }
        }

        if(popHTR && isSent) {
            PopDialog(
                onConfirm = { popHTR = false }
            ) {
                htr()
                Text(text = htrText)
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
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


fun renderSvgToBitmap(svgContent: String, quality: Int): ImageBitmap {
    val svg = SVG.getFromString(svgContent)
    val width = svg.documentWidth.toInt() * quality
    val height = svg.documentHeight.toInt() * quality
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = android.graphics.Canvas(bitmap)
    svg.renderToCanvas(canvas)
    return bitmap.asImageBitmap()
}


fun convertSvgToPng(imagesPath : String, svgPath: String, quality: Int) {
    val svg = File(svgPath)
    val bitmap = renderSvgToBitmap(svg.readText(), quality).asAndroidBitmap()
    val file = File(imagesPath, svg.nameWithoutExtension + ".png")
    file.createNewFile()
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val bitmapdata: ByteArray = stream.toByteArray()
    val fos = FileOutputStream(file)
    fos.write(bitmapdata)
    fos.flush()
    fos.close()
    stream.close()
}
