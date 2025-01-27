package isel.acrae.postchat.service

import isel.acrae.postchat.domain.Chat
import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.ChatList
import isel.acrae.postchat.domain.CreateChatInput
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.HtrResult
import isel.acrae.postchat.domain.Message
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.MessageList

interface ChatDataService {
    suspend fun getMessages(token : String): MessageList

    suspend fun getChats(token: String) : ChatList

    suspend fun getChatInfo(token: String, chatId: Int): ChatInfo

    suspend fun htrMessage(token: String, handwrittenInput: HandwrittenInput): HtrResult

    suspend fun createChat(token: String, input: CreateChatInput): Chat

    suspend fun sendMessage(token: String, input: MessageInput, chatId: Int) : Message
}