package isel.acrae.postchat.service

import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.MessageList

interface ChatDataService {
    suspend fun getMessages(token: String) : MessageList

    suspend fun getChatInfo(token: String, chatId: Int): ChatInfo

    suspend fun ocrMessage(token: String, handwrittenInput: HandwrittenInput): String

    suspend fun createChat(token: String, phoneNumbers: List<String>): ChatInfo

    suspend fun sendMessage(token: String, input: MessageInput, chatId: Int)
}