package isel.acrae.com.service

import java.util.*

fun encodeBase64(byteArray: ByteArray): String =
    Base64.getUrlEncoder().encodeToString(byteArray)

fun decodeBase64(s : String): ByteArray =
    Base64.getUrlDecoder().decode(s)