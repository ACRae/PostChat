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
import java.util.UUID

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
        return ChatList(mockChats.filter { chatIds.contains(it.id) })
    }

    override suspend fun getChatInfo(token: String, chatId: Int): ChatInfo {
        mockTokens[token]!!
        val chat = mockChats.first { it.id == chatId }
        val usersIds = mockChatUserRelation.toList().filter {
            it.second.contains(chatId)
        }.map { it.first }
        val users = usersIds.map {
            val user = mockUsers[it]!!
            UserInfo(user.phoneNumber, user.name)
        }
        return ChatInfo(
            props = Chat(chat.id, chat.name, chat.createdAt),
            usersInfo = users
        )
    }

    override suspend fun htrMessage(token: String, handwrittenInput: HandwrittenInput): String =
        "Good luck with that buddy"

    override suspend fun createChat(token: String, input: CreateChatInput): Chat {
        mockTokens[token]!!
        val newChatId = mockChats.maxBy{ it.id }.id + 1
        val chat = Chat(newChatId, input.name, input.timestamp)
        mockChats.add(chat)
        input.phoneNumbers.forEach {
            val chatIds = mockChatUserRelation[it]
            if(chatIds == null)
                mockChatUserRelation.plus(it to listOf(newChatId))
            else mockChatUserRelation.replace(it, chatIds.toMutableList().apply {
                add(newChatId) }
            )
        }
        return chat
    }

    override suspend fun sendMessage(token: String, input: MessageInput, chatId: Int): Message {
        val pn = mockTokens[token]!!
        Log.i("Before", "Before UUID")
        val newMessageId = UUID.randomUUID().toString().replace(Regex("[a-zA-Z-]"), "").take(5)
        val mergedContent = mergeBase64(mockTemplate.content, input.content)
        val message = Message(
            newMessageId.toInt(), pn,
            chatId, mergedContent, input.content,
            input.templateName, input.createdAt
        )
        mockMessages.plus(message to chatId)
        return message
    }
}