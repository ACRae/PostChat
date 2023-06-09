package isel.acrae.com.service

import isel.acrae.com.MockService
import isel.acrae.com.domain.makePhoneNumber
import isel.acrae.com.http.error.ApiIllegalArgumentException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Timestamp


class ServiceUserTest : MockService() {

    @Test
    fun getUsers() {
        runTest {
            insertTestUsers(serviceHome)
            val users = serviceUser.getUsers(tPhoneNumber(1),
                listOf(makePhoneNumber(tRegion, tPhoneNumber(2)), "test", "phonenumberx")
            )
            assertEquals(users.list.size, 1)
            assert(users.list.map { it.name }.contains(tName(2)))
        }
    }

    @Test
    fun getUserFromToken() {
        runTest {
            val (token1, token2) = insertTestUsers(serviceHome)
            val user1 = serviceUser.getUserFromToken(token1.content)
            assertEquals(user1.name, tName(1))
            val user2 = serviceUser.getUserFromToken(token2.content)
            assertEquals(user2.name, tName(2))
        }
    }

    @Test
    fun deleteUser() {
        runTest {
            val token = insertTestUsers(serviceHome, 1)
            val user1 = serviceUser.getUserFromToken(token[0].content)
            serviceUser.deleteUser(user1.phoneNumber)
            assertThrows<ApiIllegalArgumentException> {
                serviceUser.getUserFromToken(token[0].content)
            }
        }
    }

    @Test
    fun `delete multiple User`() {
        runTest {
            val token = insertTestUsers(serviceHome, 2)
            val user1 = serviceUser.getUserFromToken(token[0].content)
            val user2 = serviceUser.getUserFromToken(token[1].content)
            serviceUser.deleteUser(user1.phoneNumber)
            assertThrows<ApiIllegalArgumentException> {
                serviceUser.getUserFromToken(token[0].content)
            }

            serviceUser.deleteUser(user2.phoneNumber)
            assertThrows<ApiIllegalArgumentException> {
                serviceUser.getUserFromToken(token[1].content)
            }
        }
    }


    @Test
    fun `delete user and make chat with it`() {
        runTest {
            val token = insertTestUsers(serviceHome, 2)
            val user1 = serviceUser.getUserFromToken(token[0].content)
            val user2 = serviceUser.getUserFromToken(token[1].content)
            serviceUser.deleteUser(user1.phoneNumber)
            assertThrows<ApiIllegalArgumentException> {
                serviceChat.createChat(
                    user2.phoneNumber,
                    listOf(user1.phoneNumber),
                    "Name",
                    Timestamp(System.currentTimeMillis())
                )
            }
        }
    }
}

