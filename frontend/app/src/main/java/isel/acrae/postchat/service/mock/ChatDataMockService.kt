package isel.acrae.postchat.service.mock

import isel.acrae.postchat.domain.Chat
import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.CreateChatInput
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.service.ChatDataService
import isel.acrae.postchat.service.mock.data.mockChatUserRelation
import isel.acrae.postchat.service.mock.data.mockChats
import isel.acrae.postchat.service.mock.data.mockMessages
import isel.acrae.postchat.service.mock.data.mockTokens
import isel.acrae.postchat.service.mock.data.mockUsers
import java.sql.Timestamp

class ChatDataMockService : ChatDataService {
    override suspend fun getMessages(token: String): List<MessageEntity> {
        val pn = mockTokens[token]!!
        val chatIds = mockChatUserRelation[pn]!!
        return mockMessages.toList()
            .filter { chatIds.contains(it.first.chatTo) }
            .map { it.first }
    }

    override suspend fun getChats(token: String): List<ChatEntity> {
        val pn = mockTokens[token]!!
        val chatIds = mockChatUserRelation[pn]!!
        return mockChats.filter { chatIds.contains(it.id) }
    }

    override suspend fun getChatInfo(token: String, chatId: Int): ChatInfo {
        mockTokens[token]!!
        val chat = mockChats.first { it.id == chatId }
        val usersIds = mockChatUserRelation.toList().filter {
            it.second.contains(chatId)
        }.map { it.first }
        val users = usersIds.map {
            val user = mockUsers[it]!!
            UserInfo(user.phoneNumber, user.name, user.bio)
        }
        return ChatInfo(
            props = Chat(chat.id, chat.name, Timestamp.valueOf(chat.createdAt)),
            usersInfo = users
        )
    }

    override suspend fun ocrMessage(token: String, handwrittenInput: HandwrittenInput): String =
        "Good luck with that buddy"

    override suspend fun createChat(token: String, input: CreateChatInput): ChatEntity {
        mockTokens[token]!!
        val newChatId = mockChats.maxBy{ it.id }.id + 1
        val chat = ChatEntity(newChatId, input.name, input.timestamp.toString())
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

    override suspend fun sendMessage(token: String, input: MessageInput, chatId: Int): MessageEntity {
        val pn = mockTokens[token]!!
        val newMessageId = mockMessages.keys.maxBy{ it.id }.id + 1
        val message = MessageEntity(
            newMessageId, pn,
            chatId, input.content, "",
            input.templateName, input.createdAt.toString()
        )
        mockMessages.plus(message to chatId)
        return message
    }
}