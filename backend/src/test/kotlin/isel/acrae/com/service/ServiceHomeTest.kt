package isel.acrae.com.service

import isel.acrae.com.MockService
import isel.acrae.com.http.error.ApiIllegalArgumentException
import isel.acrae.com.http.error.ApiResourceNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class ServiceHomeTest : MockService() {

    @Test
    fun signIn() {
        runTest {
            val token = serviceHome.signIn(tName(1), tPhoneNumber(1), tRegion, tPassword(1))
            val user = serviceUser.getUserFromToken(token.content)
            assert(user.name == tName(1))
        }
    }

    @Test
    fun `signIn invalid password`() {
        assertThrows<ApiIllegalArgumentException> {
            runTest {
                serviceHome.signIn(
                    tName(1), tPhoneNumber(1),
                    tRegion, "bad password"
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
                    tRegion, tPassword(1)
                )
            }
        }
    }

    @Test
    fun `signIn invalid region`() {
        assertThrows<ApiIllegalArgumentException> {
            runTest {
                serviceHome.signIn(
                    tName(1), "912345671",
                    1, tPassword(1)
                )
            }
        }
    }

    @Test
    fun `signIn with empty name 1`() {
        assertThrows<ApiIllegalArgumentException> {
            runTest {
                serviceHome.signIn(
                    "       ", "912345671",
                    351, tPassword(1)
                )
            }
        }
    }

    @Test
    fun `signIn with empty name 2`() {
        assertThrows<ApiIllegalArgumentException> {
            runTest {
                serviceHome.signIn(
                    "", "912345671",
                    351, tPassword(1)
                )
            }
        }
    }


    @Test
    fun `signIn with invalid name`() {
        assertThrows<ApiIllegalArgumentException> {
            runTest {
                serviceHome.signIn(
                    "     H    ", "912345671",
                    351, tPassword(1)
                )
            }
        }
    }

    @Test
    fun `login with not expired token`() {
        runTest {
            val token1 = serviceHome.signIn(tName(1), tPhoneNumber(1), tRegion, tPassword(1))
            val user = serviceUser.getUserFromToken(token1.content)
            assert(user.name == tName(1))
            val token2 = serviceHome.login(tPhoneNumber(1), tRegion, tPassword(1))
            assertEquals(token1.content, token2.content)
        }
    }

    @Test
    fun `sing in token versus login token`() {
        runTest {
            val token1 = serviceHome.signIn(tName(1), tPhoneNumber(1), tRegion, tPassword(1), 1)
            val user = serviceUser.getUserFromToken(token1.content)
            assert(user.name == tName(1))
            Thread.sleep(1001)
            val token2 = serviceHome.login(tPhoneNumber(1), tRegion, tPassword(1))
            assertNotEquals(token1.content, token2.content)
        }
    }

    @Test
    fun `login with invalid token`() {
        runTest {
            assertThrows<ApiIllegalArgumentException> {
                serviceUser.getUserFromToken("")
            }
        }
    }

    @Test
    fun `sign in with invalid password`() {
        runTest {
            assertThrows<ApiIllegalArgumentException> {
                serviceHome.signIn(tName(1), tPhoneNumber(1), tRegion, "", 1)
            }
        }
    }

    @Test
    fun `log in with invalid password`() {
        runTest {
            assertThrows<ApiIllegalArgumentException> {
                serviceHome.login(tPhoneNumber(1), tRegion, "")
            }
        }
    }

    @Test
    fun `create user and log in with wrong user password`() {
        runTest {
            assertThrows<ApiResourceNotFoundException> {
                serviceHome.signIn(tName(1), tPhoneNumber(1), tRegion, tPassword(1), 1)
                serviceHome.login(tPhoneNumber(1), tRegion, "PASS1")
            }
        }
    }
}