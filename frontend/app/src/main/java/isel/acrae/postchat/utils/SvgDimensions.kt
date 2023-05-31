package isel.acrae.postchat.utils

import coil.size.Size
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun getSvgDimensions(svgPath: String): Size {
    val file = File(svgPath)
    val widthRx = Regex("""width="(\d+px)"""")
    val heightRx = Regex("""height="(\d+px)"""")
    val numberRx = Regex("""\d+""")
    val reader = BufferedReader(FileReader(file))
    var line: String? = reader.readLine()
    var height = 0
    var width = 0
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