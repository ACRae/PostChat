package isel.acrae.postchat.service

import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.MessageList

interface ChatDataService {

    @Route("/chat")
    suspend fun getMessages(token: String) : MessageList

    @Route("/chat/{id}")
    suspend fun getChatInfo(token: String, chatId: Int): ChatInfo

    @Route("/ocr")
    suspend fun ocrMessage(token: String, handwrittenInput: HandwrittenInput): String

    @Route("/chat")
    suspend fun createChat(token: String, phoneNumbers: List<String>): ChatInfo

    @Route("/chat/{id}")
    suspend fun sendMessage(token: String, input: MessageInput, chatId: Int)
}