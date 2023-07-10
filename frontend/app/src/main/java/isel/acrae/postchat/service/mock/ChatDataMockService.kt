package isel.acrae.postchat.service.mock


import android.util.Log
import isel.acrae.postchat.domain.Chat
import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.ChatList
import isel.acrae.postchat.domain.CreateChatInput
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.Message
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.MessageList
import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.service.ChatDataService
import isel.acrae.postchat.service.mock.data.mockChatUserRelation
import isel.acrae.postchat.service.mock.data.mockChats
import isel.acrae.postchat.service.mock.data.mockMessages
import isel.acrae.postchat.service.mock.data.mockTemplate
import isel.acrae.postchat.service.mock.data.mockTokens
import isel.acrae.postchat.service.mock.data.mockUsers
import isel.acrae.postchat.utils.mergeBase64
import java.sql.Timestamp

class ChatDataMockService : ChatDataService {
    override suspend fun getMessages(token: String): MessageList {
        val pn = mockTokens[token]!!
        val chatIds = mockChatUserRelation[pn]!!
        return MessageList(mockMessages.toList()
            .filter { chatIds.contains(it.first.chatTo) }
            .map { it.first })
    }

    override suspend fun getChats(token: String): ChatList {
        val pn = mockTokens[token]!!
        val chatIds = mockChatUserRelation[pn]!!
        Log.i("CHATS", mockChats.map { it.name }.toString() )
        Log.i("CHAT_REL", mockChatUserRelation.map { it.key }.toString() )
        return ChatList(mockChats.filter { chatIds.contains(it.id) })
    }

    override suspend fun getChatInfo(token: String, chatId: Int): ChatInfo {
        mockTokens[token]!!
        Log.i("GET CHAT INFO", chatId.toString())
        val chat = mockChats.first { it.id == chatId }
        Log.i("TESTTEST", "AFTER GET MOCK CHAT")
        val usersIds = mockChatUserRelation.toList().filter {
            it.second.contains(chatId)
        }.map { it.first }
        Log.i("TESTTEST", usersIds.toString())
        val users = usersIds.map { id ->
            val user = mockUsers.filter { it.value.phoneNumber == id }.values.first()
            UserInfo(user.phoneNumber, user.name)
        }
        Log.i("TESTTEST", users.toString())
        return ChatInfo(
            props = Chat(chat.id, chat.name, chat.createdAt, chat.lastMessage),
            usersInfo = users
        )
    }

    override suspend fun htrMessage(token: String, handwrittenInput: HandwrittenInput): String {
        Log.i("IN HTR", "HUM")
        return "This is just a test, HTR result will be shown here"
    }


    override suspend fun createChat(token: String, input: CreateChatInput): Chat {
        mockTokens[token]!!
        val newChatId = generateId()
        val chat = Chat(newChatId, input.name, input.timestamp)
        mockChats.add(chat)
        Log.i("PHONENUMBERS", input.phoneNumbers.toString())
        input.phoneNumbers.forEach {
            val chatIds = mockChatUserRelation[it]
            if(chatIds == null)
                mockChatUserRelation[it] = listOf(newChatId)
            else mockChatUserRelation.replace(it, chatIds.toMutableList().apply {
                add(newChatId) }
            )
        }
        return chat
    }

    override suspend fun sendMessage(token: String, input: MessageInput, chatId: Int): Message {
        Log.i("ON SEND MESSAGE MOCK", "chatId = $chatId, input = ${input.templateName}, ${input.content}, ${input.createdAt}")
        val pn = mockTokens[token]!!
        val newMessageId = generateId()
        Log.i("MESSAGE ID", newMessageId.toString())
        val mergedContent = mergeBase64(mockTemplate.content, input.content)
        val message = Message(
            newMessageId, pn,
            chatId, mergedContent, input.content,
            input.templateName, input.createdAt
        )

        mockMessages[message] = chatId
        Log.i("MOCK MESSAGES", mockMessages.toString())
        val chatIdx = mockChats.indexOfFirst { it.id == chatId }
        val newChat = mockChats[chatIdx].copy(lastMessage = Timestamp(System.currentTimeMillis()))
        mockChats[chatIdx] = newChat
        Log.i("MOCK CHATS", mockChats.toString())
        return message
    }
}

