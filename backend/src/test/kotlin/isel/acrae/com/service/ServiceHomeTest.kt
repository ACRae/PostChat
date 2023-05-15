package isel.acrae.com.service

import isel.acrae.com.MockService
import isel.acrae.com.http.error.ApiIllegalArgumentException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class ServiceHomeTest : MockService() {

    @Test
    fun signIn() {
        runTest {
            val token = serviceHome.signIn(tName(1), tPhoneNumber(1), tRegion, tPassword(1), tBio)
            val user = serviceUser.getUserFromToken(token.content)
            assert(user.name == tName(1))
            assert(user.bio == tBio)
        }
    }

    @Test
    fun `signIn invalid password`() {
        assertThrows<ApiIllegalArgumentException> {
            runTest {
                serviceHome.signIn(
                    tName(1), tPhoneNumber(1),
                    tRegion, "bad password", tBio
                )
            }
        }
    }

    @Test
    fun `signIn invalid phone number`() {
        assertThrows<ApiIllegalArgumentException> {
            runTest {
                serviceHome.signIn(
                    tName(1), "12",
                    tRegion, tPassword(1), tBio
                )
            }
        }
    }

    @Test
    fun `login with not expired token`() {
        runTest {
            val token1 = serviceHome.signIn(tName(1), tPhoneNumber(1), tRegion, tPassword(1), tBio)
            val user = serviceUser.getUserFromToken(token1.content)
            assert(user.name == tName(1))
            assert(user.bio == tBio)
            val token2 = serviceHome.login(tPhoneNumber(1), tRegion, tPassword(1))
            assertEquals(token1.content, token2.content)
        }
    }

    @Test
    fun `login with expired token`() {
        runTest {
            val token1 = serviceHome.signIn(tName(1), tPhoneNumber(1), tRegion, tPassword(1), tBio, 1)
            val user = serviceUser.getUserFromToken(token1.content)
            assert(user.name == tName(1))
            assert(user.bio == tBio)
            Thread.sleep(1001)
            val token2 = serviceHome.login(tPhoneNumber(1), tRegion, tPassword(1))
            assertNotEquals(token1.content, token2.content)
        }
    }
}