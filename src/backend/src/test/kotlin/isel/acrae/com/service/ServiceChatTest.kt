package isel.acrae.com.service

import isel.acrae.com.MockService
import isel.acrae.com.domain.Template
import isel.acrae.com.domain.makePhoneNumber
import isel.acrae.com.http.error.ApiIllegalArgumentException
import isel.acrae.com.http.error.ProblemTypeDetail
import isel.acrae.com.testContent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Time
import java.sql.Timestamp

internal class ServiceChatTest : MockService() {

    @Test
    fun `create a chat`() {
        val listToken = insertTestUsers(serviceHome, 4)
        val users = listToken.map { serviceUser.getUserFromToken(it.content) }
        val chat1 = serviceChat.createChat(
            users[0].phoneNumber,
            users.map { it.phoneNumber },
            "Chat1", Timestamp(System.currentTimeMillis())
        )
        assertEquals(chat1.name, "Chat1")
        val chatInfo1 = serviceChat.getChatInfo(users[0].phoneNumber, chat1.id)
        assertEquals(chatInfo1.usersInfo.size, 3)
    }

    @Test
    fun `create two  chats`() {
        val listToken = insertTestUsers(serviceHome, 4)
        val users = listToken.map { serviceUser.getUserFromToken(it.content) }
        val chat1 = serviceChat.createChat(
            users[0].phoneNumber,
            users.map { it.phoneNumber },
            "Chat1", Timestamp(System.currentTimeMillis())
        )
        assertEquals(chat1.name, "Chat1")
        val chatInfo1 = serviceChat.getChatInfo(users[0].phoneNumber, chat1.id)
        assertEquals(chatInfo1.usersInfo.size, 3)


        val chat2 = serviceChat.createChat(
            users[0].phoneNumber,
            users.map { it.phoneNumber },
            "Chat2", Timestamp(System.currentTimeMillis())
        )
        assertEquals(chat2.name, "Chat2")
        val chatInfo2 = serviceChat.getChatInfo(users[0].phoneNumber, chat1.id)
        assertEquals(chatInfo2.usersInfo.size, 3)
    }

    @Test
    fun `create three chats`() {
        val listToken = insertTestUsers(serviceHome, 4)
        val users = listToken.map { serviceUser.getUserFromToken(it.content) }
        val chat1 = serviceChat.createChat(
            users[0].phoneNumber,
            users.map { it.phoneNumber },
            "Chat1", Timestamp(System.currentTimeMillis())
        )
        assertEquals(chat1.name, "Chat1")
        val chatInfo1 = serviceChat.getChatInfo(users[0].phoneNumber, chat1.id)
        assertEquals(chatInfo1.usersInfo.size, 3)


        val chat2 = serviceChat.createChat(
            users[0].phoneNumber,
            users.map { it.phoneNumber },
            "Chat2", Timestamp(System.currentTimeMillis())
        )
        assertEquals(chat2.name, "Chat2")
        val chatInfo2 = serviceChat.getChatInfo(users[0].phoneNumber, chat1.id)
        assertEquals(chatInfo2.usersInfo.size, 3)

        val chat3 = serviceChat.createChat(
            users[0].phoneNumber,
            users.map { it.phoneNumber },
            "Chat2", Timestamp(System.currentTimeMillis())
        )
        assertEquals(chat3.name, "Chat2")
        val chatInfo3 = serviceChat.getChatInfo(users[0].phoneNumber, chat1.id)
        assertEquals(chatInfo3.usersInfo.size, 3)
    }

    @Test
    fun `send and get messages`() {
        runTest {
            val messageContent = testContent
            serviceTemplate.insertTestTemplate(Template.TEST)
            val (token1, token2) = insertTestUsers(serviceHome)
            val user1 = serviceUser.getUserFromToken(token1.content)
            val user2 = serviceUser.getUserFromToken(token2.content)
            val chat = serviceChat.createChat(user1.phoneNumber, listOf(user2.phoneNumber), "Test",
                Timestamp(System.currentTimeMillis())
            )
            serviceChat.sendMessage(
                user1.phoneNumber, chat.id, messageContent,
                Template.TEST.name, Timestamp(System.currentTimeMillis())
            )
            assert(
                serviceChat.getMessages(user2.phoneNumber).list.map {
                    it.handwrittenContent
                }.contains(messageContent)
            )
            assert(serviceChat.getMessages(user2.phoneNumber).list.isEmpty())
        }
    }

    @Test
    fun getMessages() {
        runTest {
            val messageContent = testContent
            serviceTemplate.insertTestTemplate(Template.TEST)
            val (token1, token2) = insertTestUsers(serviceHome)
            val user1 = serviceUser.getUserFromToken(token1.content)
            val user2 = serviceUser.getUserFromToken(token2.content)
            val chat = serviceChat.createChat(user1.phoneNumber, listOf(user2.phoneNumber), "Test",
                Timestamp(System.currentTimeMillis())
            )
            serviceChat.sendMessage(
                user1.phoneNumber, chat.id, messageContent,
                Template.TEST.name, Timestamp(System.currentTimeMillis())
            )
            assert(
                serviceChat.getMessages(user2.phoneNumber).list.map {
                    it.handwrittenContent
                }.contains(messageContent)
            )
            assert(serviceChat.getMessages(user2.phoneNumber).list.isEmpty())
        }
    }

    @Test
    fun getChats() {
        runTest {
            val messageContent = testContent
            serviceTemplate.insertTestTemplate(Template.TEST)
            val (token1, token2) = insertTestUsers(serviceHome)
            val user1 = serviceUser.getUserFromToken(token1.content)
            val user2 = serviceUser.getUserFromToken(token2.content)
            val chat = serviceChat.createChat(user1.phoneNumber, listOf(user2.phoneNumber), "Test",
                Timestamp(System.currentTimeMillis())
            )
            serviceChat.sendMessage(
                user1.phoneNumber, chat.id, messageContent,
                Template.TEST.name, Timestamp(System.currentTimeMillis())
            )
            assert(
                serviceChat.getMessages(user2.phoneNumber).list.map {
                    it.handwrittenContent
                }.contains(messageContent)
            )
            assert(serviceChat.getMessages(user2.phoneNumber).list.isEmpty())
        }
    }

    @Test
    fun getChatInfo() {
        runTest {
            val messageContent = testContent
            serviceTemplate.insertTestTemplate(Template.TEST)
            val (token1, token2) = insertTestUsers(serviceHome)
            val user1 = serviceUser.getUserFromToken(token1.content)
            val user2 = serviceUser.getUserFromToken(token2.content)
            val chat = serviceChat.createChat(user1.phoneNumber, listOf(user2.phoneNumber), "Test",
                Timestamp(System.currentTimeMillis())
            )
            serviceChat.sendMessage(
                user1.phoneNumber, chat.id, messageContent,
                Template.TEST.name, Timestamp(System.currentTimeMillis())
            )
            assert(
                serviceChat.getMessages(user2.phoneNumber).list.map {
                    it.handwrittenContent
                }.contains(messageContent)
            )
            assert(serviceChat.getMessages(user2.phoneNumber).list.isEmpty())
        }
    }

    @Test
    fun `getChatInfo - Non-existent Chat`() {
        runTest {
            val (token1, _) = insertTestUsers(serviceHome)
            val user1 = serviceUser.getUserFromToken(token1.content)
            val nonExistentChatId = 12455

            // Attempt to retrieve chat info for a non-existent chat
            val exception = assertThrows<ApiIllegalArgumentException> {
                serviceChat.getChatInfo(user1.phoneNumber, nonExistentChatId)
            }
            assertEquals(ProblemTypeDetail.CHAT_NOT_FOUND.toString(), exception.message)
        }
    }

    @Test
    fun `createChat - Invalid User`() {
        runTest {
            val (_, token2) = insertTestUsers(serviceHome)
            val user2 = serviceUser.getUserFromToken(token2.content)

            // Attempt to create a chat with an invalid user (user1)
            val exception = assertThrows<ApiIllegalArgumentException> {
                serviceChat.createChat(
                    "invalid-user",
                    listOf(user2.phoneNumber),
                    "Test",
                    Timestamp(System.currentTimeMillis())
                )
            }
            assertEquals(ProblemTypeDetail.USER_NOT_FOUND.toString(), exception.message)
        }
    }

    @Test
    fun `sendMessage - Non-existent Template`() {
        runTest {
            val (token1, _) = insertTestUsers(serviceHome)
            val user1 = serviceUser.getUserFromToken(token1.content)
            val nonExistentChatId = 123456
            val messageContent = testContent

            val exception = assertThrows<Exception> {
                serviceChat.sendMessage(
                    user1.phoneNumber,
                    nonExistentChatId,
                    messageContent,
                    Template.TEST.name,
                    Timestamp(System.currentTimeMillis())
                )
            }
            assertEquals(ProblemTypeDetail.TEMPLATE_NOT_FOUND.toString(), exception.message)
        }
    }

    @Test
    fun `getMessages - Invalid User`() {
        runTest {
            val (_, token2) = insertTestUsers(serviceHome)
            serviceUser.getUserFromToken(token2.content)

            // Attempt to retrieve messages for an invalid user (user1)
            val exception = assertThrows<Exception> {
                serviceChat.getMessages("invalid-user")
            }
            assertEquals(ProblemTypeDetail.USER_NOT_FOUND.toString(), exception.message)
        }
    }
}