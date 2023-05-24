package isel.acrae.com.http.controller

import isel.acrae.com.*
import isel.acrae.com.domain.*
import isel.acrae.com.http.Routes
import isel.acrae.com.http.input.CreateChatInput
import isel.acrae.com.http.input.CreateUserInput
import isel.acrae.com.http.input.MessageInput
import isel.acrae.com.service.ChatList
import isel.acrae.com.service.MessageList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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
                "Test"
            ),
            HttpStatus.CREATED,
            userTokenCookie
        )
        val chatRes = mapper.readValue(
            chatCreateRes.responseBodyContent, Chat::class.java
        )


        assertEquals(chatRes.name, "Test")
        val response = webTestClient.buildGet(
            Routes.Chat.CHAT,
            HttpStatus.OK,
            userTokenCookie
        )
        val chats = mapper.readValue(
            response.responseBodyContent, ChatList::class.java
        )
        assertEquals(chats.list.size, 1)
    }

    @Test
    fun `create 2 chats and 5 users and get Messages`() {
        val userTokenCookie = registerUser()
        val userPhoneNumbers = registerUsers(5)

        webTestClient.buildPost(
            Routes.Chat.CHAT, CreateChatInput(
                listOf(userPhoneNumbers.first().second),
                "Test"
            ),
            HttpStatus.CREATED,
            userTokenCookie
        )

        webTestClient.buildPost(
            Routes.Chat.CHAT, CreateChatInput(
                listOf(
                    userPhoneNumbers.first().second,
                    userPhoneNumbers[1].second
                ),
                "Test"
            ),
            HttpStatus.CREATED,
            userPhoneNumbers[2].first
        )

        val response = webTestClient.buildGet(
            Routes.Chat.CHAT, HttpStatus.OK,
            userTokenCookie
        )

        val chats = mapper.readValue(
            response.responseBodyContent, ChatList::class.java
        )

        assertEquals(chats.list.size, 1)

        val response1 = webTestClient.buildGet(
            Routes.Chat.CHAT, HttpStatus.OK,
            userPhoneNumbers.first().first
        )

        val chats1 = mapper.readValue(
            response1.responseBodyContent, ChatList::class.java
        )

        assertEquals(chats1.list.size, 2)
    }


    @Test
    fun `send and get Message`() {
        val userTokenCookie = registerUser()
        val user2PhoneNumber = registerUsers(1)

        val chatCreateRes = webTestClient.buildPost(
            Routes.Chat.CHAT, CreateChatInput(
                listOf(user2PhoneNumber.first().second),
                "Test"
            ),
            HttpStatus.CREATED,
            userTokenCookie
        )

        val chatRes = mapper.readValue(
            chatCreateRes.responseBodyContent, Chat::class.java
        )

        webTestClient.buildPost(
            chatUriId(chatRes.id.toString()),
            MessageInput(
                testContent,
                Template.TEST.name
            ),
            HttpStatus.CREATED,
            userTokenCookie
        )

        val messageRes = webTestClient.buildGet(
            Routes.Message.MESSAGE, HttpStatus.OK,
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
                "Test"
            ),
            HttpStatus.CREATED,
            userTokenCookie
        )

        val chatRes = mapper.readValue(
            chatCreateRes.responseBodyContent, Chat::class.java
        )

        repeat(2) {
            webTestClient.buildPost(
                chatUriId(chatRes.id.toString()),
                MessageInput(
                    testContent,
                    "test_template"
                ),
                HttpStatus.CREATED,
                userTokenCookie
            )
        }

        val messageRes = webTestClient.buildGet(
            Routes.Message.MESSAGE, HttpStatus.OK,
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