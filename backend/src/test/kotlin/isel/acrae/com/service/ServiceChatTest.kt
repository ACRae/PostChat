package isel.acrae.com.service

import isel.acrae.com.MockService
import isel.acrae.com.domain.Template
import isel.acrae.com.domain.makePhoneNumber
import isel.acrae.com.http.error.ApiIllegalArgumentException
import isel.acrae.com.testContent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ServiceChatTest : MockService() {

    @Test
    fun `create private Chat`() {
        val (token1, token2) = insertTestUsers(serviceHome)
        val user1 = serviceUser.getUserFromToken(token1.content)
        val user2 = serviceUser.getUserFromToken(token2.content)
        val eU2 = makePhoneNumber(tRegion, tPhoneNumber(2))
        val eU1 = makePhoneNumber(tRegion, tPhoneNumber(1))
        val chat1 = serviceChat.createChat(user1.phoneNumber, listOf(eU2), null)
        assertEquals(chat1.props.name, null)
        assertEquals(chat1.usersInfo.size, 2)
        org.junit.jupiter.api.assertThrows<ApiIllegalArgumentException> {
            serviceChat.createChat(user2.phoneNumber, listOf(eU1), null)
        }
        assertEquals(serviceChat.getChats(user1.phoneNumber).list.size, 1)
    }

    @Test
    fun `create public chat`() {
        val listToken = insertTestUsers(serviceHome, 4)
        val users = listToken.map { serviceUser.getUserFromToken(it.content) }
        val chat1 = serviceChat.createChat(
            users[0].phoneNumber,
            users.map { it.phoneNumber },
            "Chat1"
        )
        assertEquals(chat1.props.name, "Chat1")
        assertEquals(chat1.usersInfo.size, 4)
    }

    @Test
    fun `send and get messages`() {
        runTest {
            val messageContent = testContent
            serviceTemplate.insertTestTemplate(Template.TEST)
            val (token1, token2) = insertTestUsers(serviceHome)
            val user1 = serviceUser.getUserFromToken(token1.content)
            val user2 = serviceUser.getUserFromToken(token2.content)
            val chat = serviceChat.createChat(user1.phoneNumber, listOf(user2.phoneNumber), null)
            serviceChat.sendMessage(
                user1.phoneNumber, chat.props.id, messageContent, Template.TEST.name
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