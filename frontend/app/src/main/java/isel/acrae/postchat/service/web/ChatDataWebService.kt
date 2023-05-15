package isel.acrae.postchat.service.web

import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.MessageList
import isel.acrae.postchat.service.ChatDataService
import okhttp3.OkHttpClient

class ChatDataWebService(
    baseUrl: String,
    private val httpClient: OkHttpClient,
) : ChatDataService, Web(baseUrl) {

    @Route("/chat")
    override suspend fun getMessages(token: String): MessageList =
        buildRequest(Get(makeURL(::getMessages)), token)
            .send(httpClient) { it.handle() }

    @Route("/chat/{id}")
    override suspend fun getChatInfo(token: String, chatId: Int): ChatInfo =
        buildRequest(Get(makeURL(::getChatInfo, chatId.toString())), token)
            .send(httpClient) { it.handle() }

    @Route("/ocr")
    override suspend fun ocrMessage(token: String, handwrittenInput: HandwrittenInput): String =
        buildRequest(Post(makeURL(::ocrMessage), handwrittenInput), token)
            .send(httpClient) { it.handle() }

    @Route("/chat")
    override suspend fun createChat(token: String, phoneNumbers: List<String>): ChatInfo =
        buildRequest(Post(makeURL(::createChat), phoneNumbers), token)
            .send(httpClient) { it.handle() }

    @Route("/chat/{id}")
    override suspend fun sendMessage(token: String, input: MessageInput, chatId: Int) : Unit =
        buildRequest(Post(makeURL(::sendMessage, chatId.toString()), input), token)
            .send(httpClient) { it.handle() }
}