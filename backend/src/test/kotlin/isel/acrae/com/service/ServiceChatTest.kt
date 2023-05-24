package isel.acrae.com.service

import isel.acrae.com.MockService
import isel.acrae.com.domain.Template
import isel.acrae.com.domain.makePhoneNumber
import isel.acrae.com.http.error.ApiIllegalArgumentException
import isel.acrae.com.testContent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.Time
import java.sql.Timestamp

internal class ServiceChatTest : MockService() {

    @Test
    fun `create public chat`() {
        val listToken = insertTestUsers(serviceHome, 4)
        val users = listToken.map { serviceUser.getUserFromToken(it.content) }
        val chat1 = serviceChat.createChat(
            users[0].phoneNumber,
            users.map { it.phoneNumber },
            "Chat1", Timestamp(System.currentTimeMillis())
        )
        assertEquals(chat1.name, "Chat1")
        val chatInfo1 = serviceChat.getChatInfo(users[0].phoneNumber, chat1.id)
        assertEquals(chatInfo1.usersInfo.size, 4)
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
    }

    @Test
    fun getChats() {
    }

    @Test
    fun getChatInfo() {
    }

    @Test
    fun ocrMessage() {
    }

    @Test
    fun createChat() {
    }

    @Test
    fun sendMessage() {
    }
}