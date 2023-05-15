package isel.acrae.com.domain

import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.*

object SHA256 {
    const val schemeName = "SHA-256"
    const val bytes = 256/8
}

fun UUID.toByteArray(): ByteArray {
    val bb = ByteBuffer.wrap(ByteArray(16))
    bb.putLong(mostSignificantBits)
    bb.putLong(leastSignificantBits)
    return bb.array()
}

fun ByteArray.toUUID(): UUID {
    val byteBuffer: ByteBuffer = ByteBuffer.wrap(this)
    val high: Long = byteBuffer.long
    val low: Long = byteBuffer.long
    return UUID(high, low)
}

fun encodePassword(password: String, phoneNumber: String, region: Int): String =
    Base64.getUrlEncoder().encodeToString(
        MessageDigest.getInstance(SHA256.schemeName).digest(
            password.toByteArray() + phoneNumber.toByteArray() + region.toByte()
        )
    )

fun generateBase64Token(): String =
    Base64.getUrlEncoder().encodeToString(
        MessageDigest.getInstance(SHA256.schemeName).digest(
            UUID.randomUUID().toByteArray()
        )
    )

fun String.canBeToken() =
    try{
        Base64.getUrlDecoder().decode(this).size == SHA256.bytes
    } catch (ex : IllegalArgumentException) {
        false
    }

fun String.isValidPassword() =
    isNotBlank() &&
            length in 4..30 &&
            any { it.isUpperCase() } &&
            any { it.isDigit() }

fun isValidPhoneNumber(phoneNumber: String, region: Int):Boolean {
    val util = PhoneNumberUtil.getInstance()
    val number = try {
        util.parse(
            phoneNumber,
            util.getRegionCodeForCountryCode(region)
        )
    } catch (e: NumberParseException) {
        return false
    }
    return util.isValidNumber(number)
}

fun makePhoneNumber(region: Int, number: String) =
    "$region$number"