package isel.acrae.postchat.service.mock

import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.ChatList
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.Message
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.MessageList
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.service.ChatDataService

class ChatDataMockService : ChatDataService {
    override suspend fun getMessages(token: String): List<MessageEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getChats(token: String): List<ChatEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getChatInfo(token: String, chatId: Int): ChatInfo {
        TODO("Not yet implemented")
    }

    override suspend fun ocrMessage(token: String, handwrittenInput: HandwrittenInput): String {
        TODO("Not yet implemented")
    }

    override suspend fun createChat(token: String, phoneNumbers: List<String>): ChatEntity {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(token: String, input: MessageInput, chatId: Int): MessageEntity {
        TODO("Not yet implemented")
    }
}