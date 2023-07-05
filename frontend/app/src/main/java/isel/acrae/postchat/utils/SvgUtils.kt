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
import java.nio.file.Files
import java.util.Base64
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun Float.convertToSRGB256() = (this.coerceIn(0.0f, 1.0f) * 255).toInt()
fun Color.toRGBString() =
    "rgb(${red.convertToSRGB256()}," +
        "${green.convertToSRGB256()}," +
        "${blue.convertToSRGB256()})"


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

    Log.i("COLORS", paths.map { it.properties.color }.toString())

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

    Log.i("SVG", svgBuilder.toString())

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

/**
 * Merge two SVG files into one.
 */
fun mergeBase64(b64Svg1 : String, b64Svg2 : String) : String {
    val svg1Temp = createTempSvg(b64Svg1)
    val svg2Temp = createTempSvg(b64Svg2)

    // Load the first SVG document
    val dbf = DocumentBuilderFactory.newInstance()
    val dBuilder = dbf.newDocumentBuilder()

    val svg1 = dBuilder.parse(svg1Temp)
    // Load the second SVG document
    val svg2 = dBuilder.parse(svg2Temp)

    // Get the root element of the first SVG document
    val root1 = svg1.documentElement

    // Import the contents of the second SVG document into the first SVG document
    val imported = svg1.importNode(svg2.documentElement, true)
    root1.appendChild(imported)

    // Save the merged SVG to a file
    val transformer = TransformerFactory.newInstance().newTransformer()
    val source = DOMSource(svg1)
    val mergedSvg = Files.createTempFile("temp-svg-", ".svg").toFile()
    val result = StreamResult(mergedSvg)
    transformer.transform(source, result)
    val output = Base64.getUrlEncoder()
        .encodeToString(mergedSvg.readBytes())

    svg1Temp.delete()
    svg2Temp.delete()
    mergedSvg.delete()
    return  output
}

/**
 * Create a temporary SVG file.
 */
private fun createTempSvg(b64Svg: String): File {
    val svgBytes = decodeBase64(b64Svg)
    val file1 = Files.createTempFile(null, ".svg")
    Files.write(file1, svgBytes)
    return file1.toFile()
}


fun decodeBase64(s : String): ByteArray =
    Base64.getUrlDecoder().decode(s)