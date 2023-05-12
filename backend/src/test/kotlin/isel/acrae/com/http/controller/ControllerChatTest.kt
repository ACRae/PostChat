package isel.acrae.com.http.controller

import isel.acrae.com.*
import isel.acrae.com.domain.ChatInfo
import isel.acrae.com.domain.Template
import isel.acrae.com.domain.UserInfo
import isel.acrae.com.domain.makePhoneNumber
import isel.acrae.com.http.Routes
import isel.acrae.com.http.input.CreateChatInput
import isel.acrae.com.http.input.CreateUserInput
import isel.acrae.com.http.input.MessageInput
import isel.acrae.com.service.ChatList
import isel.acrae.com.service.MessageList
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.http.HttpStatus

internal class ControllerChatTest : MockController() {
    val chatUriId = fun(id : String) =
        Routes.Chat.CHAT_ID_URI.replace("{id}", id)

    @Test
    fun `get empty Chats`() {
        val userTokenCookie = registerUser()
        val response = webTestClient.buildGet(
            Routes.Chat.CHAT, HttpStatus.OK,
            userTokenCookie
        )
        val chats = mapper.readValue(
            response.responseBodyContent, ChatList::class.java
        )
        assert(chats.list.isEmpty())
    }


    @Test
    fun `create chat between 2 users and get Messages`() {
        val userTokenCookie = registerUser()
        val user2PhoneNumber = registerUsers(1).map { it.second }

        val chatCreateRes = webTestClient.buildPost(
            Routes.Chat.CHAT, CreateChatInput(
                listOf(user2PhoneNumber.first()),
                null
            ),
            HttpStatus.CREATED,
            userTokenCookie
        )
        val chatRes = mapper.readValue(
            chatCreateRes.responseBodyContent, ChatInfo::class.java
        )

        assertEquals(chatRes.usersInfo.size, 2)
        assert(chatRes.usersInfo.map { it.phoneNumber }
            .contains(user2PhoneNumber.first()))

        val response = webTestClient.buildGet(
            Routes.Chat.CHAT,
            HttpStatus.OK,
            userTokenCookie
        )
        val chats = mapper.readValue(
            response.responseBodyContent, ChatList::class.java
        )
        assertEquals(chats.list.size, 0)
    }

    @Test
    fun `create 2 chats and 5 users and get Messages`() {
        val userTokenCookie = registerUser()
        val user2PhoneNumber = registerUsers(5)

        val chatCreateRes = webTestClient.buildPost(
            Routes.Chat.CHAT, CreateChatInput(
                listOf(user2PhoneNumber.first().second),
                null
            ),
            HttpStatus.CREATED,
            userTokenCookie
        )

        webTestClient.buildPost(
            Routes.Chat.CHAT, CreateChatInput(
                listOf(user2PhoneNumber.first().second),
                null
            ),
            HttpStatus.CREATED,
            user2PhoneNumber[2].first
        )

        val chatRes = mapper.readValue(
            chatCreateRes.responseBodyContent, ChatInfo::class.java
        )

        assertEquals(chatRes.usersInfo.size, 2)
        assert(chatRes.usersInfo.map { it.phoneNumber }
            .contains(user2PhoneNumber.first().second))

        val response = webTestClient.buildGet(
            Routes.Chat.CHAT, HttpStatus.OK,
            userTokenCookie
        )
        val chats = mapper.readValue(
            response.responseBodyContent, ChatList::class.java
        )
        assertEquals(chats.list.size, 0)
    }


    @Test
    fun `send and get Message`() {
        val userTokenCookie = registerUser()
        val user2PhoneNumber = registerUsers(1)

        val chatCreateRes = webTestClient.buildPost(
            Routes.Chat.CHAT, CreateChatInput(
                listOf(user2PhoneNumber.first().second),
                null
            ),
            HttpStatus.CREATED,
            userTokenCookie
        )

        val chatRes = mapper.readValue(
            chatCreateRes.responseBodyContent, ChatInfo::class.java
        )

        webTestClient.buildPost(
            chatUriId(chatRes.props.id.toString()),
            MessageInput(
                testContent,
                Template.TEST.name
            ),
            HttpStatus.CREATED,
            userTokenCookie
        )

        val messageRes = webTestClient.buildGet(
            Routes.Chat.CHAT, HttpStatus.OK,
            user2PhoneNumber.first().first
        )

        val messages = mapper.readValue(
            messageRes.responseBodyContent, MessageList::class.java
        )

        assertEquals(messages.list.size, 1)
    }


    @Test
    fun `send and get 2 Messages`() {
        val userTokenCookie = registerUser()
        val user2PhoneNumber = registerUsers(1)

        val chatCreateRes = webTestClient.buildPost(
            Routes.Chat.CHAT, CreateChatInput(
                listOf(user2PhoneNumber.first().second),
                null
            ),
            HttpStatus.CREATED,
            userTokenCookie
        )

        val chatRes = mapper.readValue(
            chatCreateRes.responseBodyContent, ChatInfo::class.java
        )

        repeat(2) {
            webTestClient.buildPost(
                chatUriId(chatRes.props.id.toString()),
                MessageInput(
                    testContent,
                    "test_template"
                ),
                HttpStatus.CREATED,
                userTokenCookie
            )
        }

        val messageRes = webTestClient.buildGet(
            Routes.Chat.CHAT, HttpStatus.OK,
            user2PhoneNumber.first().first
        )

        val response = webTestClient.buildGet(
            Routes.User.USER_PHONE_URI.replace("{phone}",
                makePhoneNumber(CreateUserInput.TEST.region,
                    CreateUserInput.TEST.number)
            ), HttpStatus.OK,
            userTokenCookie
        )
        val user = mapper.readValue(
            response.responseBodyContent, UserInfo::class.java
        )

        val messages = mapper.readValue(
            messageRes.responseBodyContent, MessageList::class.java
        )

        assertEquals(messages.list.size, 2)
        assert(messages.list.map { it.handwrittenContent }.contains(testContent))
        assert(messages.list.map { it.userFrom }.contains(user.phoneNumber))
    }

    @Test
    fun getMessages() {
    }

    @Test
    fun createChat() {
    }

    @Test
    fun sendMessage() {
    }

    @Test
    fun getChatInfo() {
    }
}