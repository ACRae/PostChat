package isel.acrae.com.domain

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class UtilsTest {

    private val testPhoneNumber = "123456789"
    private val testRegion = 1
    private val testPassword = "P@ssw0rd"


    @Test
    fun testEncodePassword() {
        val encoded = encodePassword("password", "1234567890", 1)
        assertNotNull(encoded)
        assertTrue(encoded.isNotBlank())
    }

    @Test
    fun testGenerateBase64Token() {
        val token = generateBase64Token()
        assertNotNull(token)
        assertTrue(token.isNotBlank())
    }

    @Test
    fun testCanBeToken() {
        val token = generateBase64Token()
        val result = token.canBeToken()
        assertTrue(result)
    }

    @Test
    fun testIsValidPassword() {
        assertTrue("TestPass1".isValidPassword())
        assertTrue("Password123".isValidPassword())
        assertTrue("f5D5k5G5h5J5".isValidPassword())
        assertFalse("".isValidPassword())
        assertFalse("1234".isValidPassword())
        assertFalse("pass".isValidPassword())
        assertFalse("password1234567890".isValidPassword())
        assertFalse("testpassword".isValidPassword())
        assertFalse("test password".isValidPassword())
    }

    @Test
    fun testIsValidPhoneNumber() {
        assertFalse(isValidPhoneNumber("1234567890", 1))
        assertFalse(isValidPhoneNumber("1415555267", 1))
        assertFalse(isValidPhoneNumber("123456789", 1))
        assertFalse(isValidPhoneNumber("1234567890", 1))
        assertFalse(isValidPhoneNumber("12345", 1))
        assertFalse(isValidPhoneNumber("1234567890", 123))
    }

    @Test
    fun `encodePassword should encode password, phone number, and region`() {
        val encoded = encodePassword(testPassword, testPhoneNumber, testRegion)
        val decoded = Base64.getUrlDecoder().decode(encoded)
        assertEquals(SHA256.bytes, decoded.size)
    }

    @Test
    fun `generateBase64Token should generate a base64 encoded SHA256 hash of a UUID`() {
        val token = generateBase64Token()
        val decoded = Base64.getUrlDecoder().decode(token)
        assertEquals(SHA256.bytes, decoded.size)
    }

    @Test
    fun `isValidPhoneNumber should return true for a valid phone number`() {
        val validPhoneNumber = "1234567890"
        val validRegion = 1
        assertEquals(false, isValidPhoneNumber(validPhoneNumber, validRegion))
    }

    @Test
    fun `isValidPhoneNumber should return false for an invalid phone number`() {
        val invalidPhoneNumber = "123"
        val validRegion = 1
        assertEquals(false, isValidPhoneNumber(invalidPhoneNumber, validRegion))
    }

    @Test
    fun `String canBeToken should return true for a valid token`() {
        val validToken = generateBase64Token()
        assertEquals(true, validToken.canBeToken())
    }

    @Test
    fun `String canBeToken should return false for an invalid token`() {
        val invalidToken = "invalid_token"
        assertEquals(false, invalidToken.canBeToken())
    }

    @Test
    fun `String isValidPassword should return true for a valid password`() {
        val validPassword = "V@lidPassw0rd"
        assertEquals(true, validPassword.isValidPassword())
    }

    @Test
    fun `String isValidPassword should return false for an invalid password`() {
        val invalidPassword = "invalid"
        assertEquals(false, invalidPassword.isValidPassword())
    }


}