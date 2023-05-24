package isel.acrae.postchat.activity.draw

import android.content.res.Resources
import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Redo
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.withSave
import isel.acrae.postchat.R
import isel.acrae.postchat.activity.draw.utils.PaintProperties
import isel.acrae.postchat.activity.draw.utils.PathProperties
import isel.acrae.postchat.ui.composable.ColorPicker
import isel.acrae.postchat.ui.composable.ExpandableFAB
import isel.acrae.postchat.ui.composable.SmallExpandableFABItem
import isel.acrae.postchat.ui.composable.SmallIconFab
import isel.acrae.postchat.ui.composable.loadImageVector
import isel.acrae.postchat.utils.zoomPanOrDrag
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

enum class MotionType {
    UP, DOWN, MOVE, UNSPECIFIED
}

data class Dimensions(
    private val width: Int,
    private val height: Int,
    private val density: Float
) {
    val widthDp = (width / density).dp
    val heightDp = (height / density).dp
}


@Composable
fun DrawScreen(
    pathPropertiesList: () -> List<PathProperties>,
    onAddPath: (PathProperties) -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onClear: () -> Unit,
    onResetUndo: () -> Unit,
) {
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity

    val image = loadImageVector(id = R.drawable.postcard) //get it from another activity

    val scaledCanvas = (screenWidth.toFloat() / image.defaultWidth.value) - 0.1f
    val scale = remember { mutableStateOf(scaledCanvas) }
    val pan = remember { mutableStateOf(Offset.Zero) }
    val paintProperties = remember { mutableStateOf(PaintProperties()) }

    val dimensions = Dimensions(
        image.defaultWidth.value.toInt(),
        image.defaultHeight.value.toInt(),
        scaledDensity
    )

    Column(
        Modifier
            .fillMaxSize()
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
                    .height(dimensions.heightDp)
                    .width(dimensions.widthDp)
                    .border(1.dp, MaterialTheme.colorScheme.outline)
                    .clipToBounds(),
            ) {
                MyCanvas(
                    image = image,
                    pathPropertiesList = pathPropertiesList,
                    onZoomOrPan = { zoom, _pan ->
                        val aux = scale.value * zoom
                        if (aux >= scaledCanvas - 0.5f)
                            scale.value *= zoom
                        pan.value += _pan
                    },
                    onAddPath = { onAddPath(it) },
                    currPaintProperties = paintProperties.value,
                    resetUndo = onResetUndo
                )
            }
        }
    }
    ExpandableFAB(
        "Edit", Icons.Default.Create,
        secondaryContent = {
            SmallIconFab(icon = Icons.Outlined.Undo, onClick = onUndo)
            SmallIconFab(icon = Icons.Outlined.Redo, onClick = onRedo)
        }
    ) {
        SmallExpandableFABItem(description = "Erase all", icon = Icons.Default.DeleteSweep, onClick = onClear)
        ColorPicker(paint = { paintProperties.value }) {
            paintProperties.value = it
        }
        SmallExpandableFABItem(description = "Reset position", icon = Icons.Default.Adjust) {
            scale.value = scaledCanvas; pan.value = Offset.Zero
        }
        //SaveCanvasButton(image, pathPropertiesList) TODO
    }
}

@Composable
fun MyCanvas(
    image: ImageVector,
    pathPropertiesList: () -> List<PathProperties>,
    currPaintProperties: PaintProperties,
    onZoomOrPan: (zoom: Float, pan: Offset) -> Unit,
    onAddPath: (PathProperties) -> Unit,
    resetUndo: () -> Unit,
) {
    //simple val to display paths with no screen tearing its content is temporary
    val auxPathPropertiesList = pathPropertiesList().toMutableList()

    var currPathProps by remember { mutableStateOf(PathProperties()) }
    var currPos by remember { mutableStateOf(Offset.Unspecified) }
    var prevPos by remember { mutableStateOf(Offset.Unspecified) }
    var motionType by remember { mutableStateOf(MotionType.UNSPECIFIED) }

    val painter = rememberVectorPainter(image = image)

    //set values for each recomposition
    currPathProps = currPathProps.copy(properties = currPaintProperties)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    zoomPanOrDrag(
                        onZoomOrPan = onZoomOrPan,
                        onDragStart = {
                            motionType = MotionType.DOWN
                            currPos = it.position
                            if (it.pressed != it.previousPressed) it.consume()
                        },
                        onDrag = {
                            motionType = MotionType.MOVE
                            currPos = it.position
                            if (it.positionChange() != Offset.Zero) it.consume()

                        },
                        onDragEnd = {
                            motionType = MotionType.UP
                            if (it.pressed != it.previousPressed) it.consume()
                        }
                    )
                }
            }

    ) {
        with(painter) { draw(size) }
        if (currPos != Offset.Unspecified) {
            when (motionType) {
                MotionType.DOWN -> {
                    currPathProps.path.moveTo(currPos.x, currPos.y)
                    prevPos = currPos
                }

                MotionType.MOVE -> {
                    currPathProps.path.quadraticBezierTo(
                        prevPos.x, prevPos.y,
                        (prevPos.x + currPos.x) / 2,
                        (prevPos.y + currPos.y) / 2
                    )
                    prevPos = currPos
                }

                MotionType.UP -> {
                    if (prevPos != Offset.Unspecified && !currPathProps.isEmpty) {
                        currPathProps.path.lineTo(currPos.x, currPos.y)

                        onAddPath(
                            PathProperties(
                                currPathProps.path,
                                currPathProps.properties
                            )
                        )
                        auxPathPropertiesList.add(
                            PathProperties(currPathProps.path, currPathProps.properties)
                        )
                        resetUndo.invoke()
                        currPathProps = PathProperties(properties = currPathProps.properties)
                        currPos = Offset.Unspecified
                        prevPos = currPos
                        motionType = MotionType.UNSPECIFIED
                    }
                }

                else -> Unit
            }
        }
        drawContext.canvas.nativeCanvas.withSave {
            //use this list variable instead in order to avoid tearing from screen refresh
            auxPathPropertiesList.forEach {
                drawPath(
                    color = it.properties.color, path = it.path,
                    style = it.properties.stroke,
                    blendMode = it.properties.blendMode
                )
            }
            if (motionType != MotionType.UNSPECIFIED) {
                drawPath(
                    color = currPathProps.properties.color,
                    path = currPathProps.path,
                    style = currPathProps.properties.stroke,
                    blendMode = currPathProps.properties.blendMode
                )
            }
        }
    }
}


@Composable
fun SaveCanvasButton(size: ImageBitmap, paths: () -> List<PathProperties>) {
    // Create a new bitmap with the desired width and height
    val bitmap = Bitmap.createBitmap(size.width, size.height, Bitmap.Config.ARGB_8888)
    // Create a new canvas using the bitmap
    val canvas = android.graphics.Canvas(bitmap)
    canvas.drawColor(-1)

    val context = LocalContext.current

    paths().forEach {
        canvas.drawPath(
            it.androidPath,
            it.frameworkPaint
        )
    }
    SmallExpandableFABItem(
        description = "Save",
        icon = Icons.Default.Send,
        onClick = {
            // Save the bitmap to a file
            val file = File(context.cacheDir, "my_file.jpg")
            file.createNewFile()
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
            val bitmapdata: ByteArray = stream.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            stream.close()
        }
    )
}