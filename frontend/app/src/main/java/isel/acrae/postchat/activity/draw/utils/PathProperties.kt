package isel.acrae.postchat.activity.draw.utils

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.core.graphics.flatten

/**
 * Stores properties for android paint.
 *
 * Keep in mind this class only uses [Stroke] drawing style.
 * @param stroke the [Stroke] to use.
 * @param color the [Color] to use.
 * @param alpha Opacity to be applied to the path from 0.0f to 1.0f representing
 * fully transparent to fully opaque respectively
 */
data class PaintProperties(
    val stroke: Stroke = Stroke(
        width = 10f,
        cap = StrokeCap.Round,
        join = StrokeJoin.Round
    ),
    val color: Color = Color.Black,
    val alpha: Float = 1f,
    val blendMode: BlendMode = DrawScope.DefaultBlendMode
) {
    companion object {
        fun eraser(stroke: Stroke) =
            PaintProperties(
                stroke,
                color = Color.Transparent,
                blendMode = BlendMode.Clear,
                alpha = 0f,
            )
    }
}

/**
 * Represents a path ands its properties.
 * @param path the path to store.
 * @param properties the properties of the path. See [PathProperties] for more details.
 *
 */
data class PathProperties(
    val path : Path = Path(),
    val properties : PaintProperties = PaintProperties()
) {
    val androidPath = path.asAndroidPath()
    val frameworkPaint = getPropPaint()
    val isEmpty : Boolean
        get() = androidPath.flatten().firstOrNull() == null

    private fun getPropPaint() = Paint().apply {
        this.style = PaintingStyle.Stroke
        this.strokeWidth = properties.stroke.width
        this.color = properties.color
        this.strokeJoin = properties.stroke.join
        this.strokeCap = properties.stroke.cap
        this.alpha = properties.alpha
        this.blendMode = properties.blendMode
    }.asFrameworkPaint()
}
