package isel.acrae.postchat.service.mock

import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.MessageList
import isel.acrae.postchat.service.ChatDataService

class ChatDataServiceMock : ChatDataService {
    override suspend fun getMessages(token: String): MessageList {
        TODO("Not yet implemented")
    }

    override suspend fun getChatInfo(token: String, chatId: Int): ChatInfo {
        TODO("Not yet implemented")
    }

    override suspend fun ocrMessage(token: String, handwrittenInput: HandwrittenInput): String {
        TODO("Not yet implemented")
    }

    override suspend fun createChat(token: String, phoneNumbers: List<String>): ChatInfo {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(token: String, input: MessageInput, chatId: Int) {
        TODO("Not yet implemented")
    }
}