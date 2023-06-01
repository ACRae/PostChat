package isel.acrae.postchat.utils

import android.graphics.PathMeasure
import android.util.Log
import androidx.compose.ui.graphics.Color
import coil.size.Size
import isel.acrae.postchat.activity.postcard.draw.utils.PathProperties
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException


fun Color.toRGBString() = "rgb(${red.toInt()},${green.toInt()},${blue.toInt()})"


fun getSvgDimensions(svgPath: String): Size {
    Log.i("PATH", svgPath)
    val file = File(svgPath)
    val widthRx = Regex("""width="(\d+px)"""")
    val heightRx = Regex("""height="(\d+px)"""")
    val numberRx = Regex("""\d+""")
    val reader = BufferedReader(FileReader(file))
    var line: String? = reader.readLine()
    var height = 402
    var width = 602
    while (line != null) {
        if(line.contains(widthRx) && line.contains(heightRx)){
            val wMatch = widthRx.find(line)
            val hMatch = heightRx.find(line)
            if(wMatch != null && hMatch != null) {
                height = numberRx.find(hMatch.groupValues.first())!!
                    .groupValues.first().toInt()
                width = numberRx.find(wMatch.groupValues.first())!!
                    .groupValues.first().toInt()
                break
            }
        }
        line = reader.readLine()
    }
    reader.close()
    return Size(width, height)
}

fun savePathsAsSvg(paths: List<PathProperties>, filePath: String, width: Int, height: Int) {
    try {
        val fileWriter = FileWriter(filePath)
        fileWriter.write(generateSvgFromPaths(paths, width, height))
        fileWriter.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}


fun savePathsAsTempSvg(
    suffix: String,
    paths: List<PathProperties>, width: Int, height: Int
): String {
    try {
        val file = File.createTempFile(suffix, ".svg")
        file.createNewFile()
        file.writeText(generateSvgFromPaths(paths, width, height))
        return file.absolutePath
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ""
}

fun convertPathToSvgByteArray(paths: List<PathProperties>, width: Int, height: Int) =
    generateSvgFromPaths(paths, width, height).toByteArray()

private fun generateSvgFromPaths(paths: List<PathProperties>, width: Int, height: Int): String {
    val svgBuilder = StringBuilder()

    // Create an SVG root element
    svgBuilder.append("<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"$width\" height=\"$height\">")

    // Iterate over the paths
    for (path in paths) {
        svgBuilder.append("<path fill=\"none\" stroke=\"${path.properties.color.toRGBString()}\" " +
                "stroke-width=\"${path.properties.stroke.width}\" " +
                "stroke-linecap=\"${path.properties.stroke.cap.toString().lowercase()}\" " +
                "stroke-linejoin=\"${path.properties.stroke.join.toString().lowercase()}\" d=\""
        )
        .append(generateSvgPathData(path))
        .append("\" />")
    }

    // Close the SVG root element
    svgBuilder.append("</svg>")

    return svgBuilder.toString()
}

private fun generateSvgPathData(path: PathProperties): String {
    val pathDataBuilder = StringBuilder()
    val pathMeasure = PathMeasure(path.androidPath, false)
    val coords = FloatArray(2)
    val length = pathMeasure.length
    var distance = 0f
    val increment = 3f
    while (distance < length) {
        pathMeasure.getPosTan(distance, coords, null)
        if (distance == 0f) {
            pathDataBuilder.append("M").append(coords[0]).append(",").append(coords[1])
        } else {
            pathDataBuilder.append("L").append(coords[0]).append(",").append(coords[1])
        }
        distance += increment
    }

    return pathDataBuilder.toString()
}