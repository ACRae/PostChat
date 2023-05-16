package isel.acrae.postchat.service

import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.ChatList
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.Message
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.MessageList
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity

interface ChatDataService {
    suspend fun getMessages(token : String): List<MessageEntity>

    suspend fun getChats(token: String) : List<ChatEntity>

    suspend fun getChatInfo(token: String, chatId: Int): ChatInfo

    suspend fun ocrMessage(token: String, handwrittenInput: HandwrittenInput): String

    suspend fun createChat(token: String, phoneNumbers: List<String>): ChatEntity

    suspend fun sendMessage(token: String, input: MessageInput, chatId: Int) : MessageEntity
}