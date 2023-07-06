package isel.acrae.com.svg

import isel.acrae.com.logger.logger
import isel.acrae.com.service.decodeBase64
import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.jetbrains.annotations.TestOnly
import java.awt.Color
import java.io.*
import java.nio.file.Files
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult


const val SVG_EXTENSION = "svg"

/**
 * @author acrae
 * Handles processing of SVG files.
 * Takes use of subprocesses and
 */
object SvgProcessing {

    private val logger = logger<SvgProcessing>()

    private val pySrc = System.getenv("PYTOOLS_POSTCHAT")

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
     * HTR an SVG file.
     * HTR is a tool detect text in an image.
     */
    fun htr(b64HwSvg : String): String {
        logger.info("STARTED HTR")
        logger.info(b64HwSvg.take(8))
        val bytes = decodeBase64(b64HwSvg)
        val pngFile = convertToPng(bytes)
        val pb = ProcessBuilder(
            "python", "main.py", "--source", pngFile.absolutePath
        ).directory( File(pySrc) )
        val process = pb.start()
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val errReader = BufferedReader(InputStreamReader(process.errorStream))
        val output = reader.readText()
        val errOutput = errReader.readText()
        process.waitFor()
        logger.error(errOutput)
        return output.also {
            reader.close()
            logger.info(it.take(5))
            logger.info("FINISHED OCR")
        }
    }

    /**
     * Convert SVG file to PNG.
     */
    private fun convertToPng(svgData: ByteArray): File {
        val transcoder = PNGTranscoder()
        transcoder.addTranscodingHint(PNGTranscoder.KEY_BACKGROUND_COLOR, Color.white)
        val input = TranscoderInput(ByteArrayInputStream(svgData))
        val outputStream = ByteArrayOutputStream()
        val output = TranscoderOutput(outputStream)
        transcoder.transcode(input, output)
        val file1 = Files.createTempFile("convert-", ".png")
        Files.write(file1, outputStream.toByteArray())
        return file1.toFile().also {
            outputStream.close()
            input.inputStream.close()
        }
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
}

@TestOnly
fun main() {
    println(Base64.getUrlEncoder().encodeToString(
        File("./templates/test/test.svg").readBytes())
    )
}