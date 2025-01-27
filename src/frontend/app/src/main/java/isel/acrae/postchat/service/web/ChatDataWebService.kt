package isel.acrae.postchat.service.web

import isel.acrae.postchat.domain.Chat
import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.ChatList
import isel.acrae.postchat.domain.CreateChatInput
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.HtrResult
import isel.acrae.postchat.domain.Message
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.MessageList
import isel.acrae.postchat.service.ChatDataService
import okhttp3.OkHttpClient

class ChatDataWebService(
    baseUrl: String,
    private val httpClient: OkHttpClient,
    private val httpHtrClient: OkHttpClient,
) : ChatDataService, Web(baseUrl) {


    @Route("/chat")
    override suspend fun getChats(token: String): ChatList =
        buildRequest(Get(makeURL(::getChats)), token)
            .send(httpClient) { it.handle() }


    @Route("/message")
    override suspend fun getMessages(token : String): MessageList =
        buildRequest(Get(makeURL(::getMessages)), token)
            .send(httpClient) { it.handle() }

    @Route("/chat/{id}")
    override suspend fun getChatInfo(token: String, chatId: Int): ChatInfo =
        buildRequest(Get(makeURL(::getChatInfo, chatId.toString())), token)
            .send(httpClient) { it.handle() }


    @Route("/htr")
    override suspend fun htrMessage(token: String, handwrittenInput: HandwrittenInput): HtrResult =
        buildRequest(Post(makeURL(::htrMessage), handwrittenInput), token)
            .send(httpHtrClient) { it.handle() }


    @Route("/chat")
    override suspend fun createChat(token: String, input: CreateChatInput): Chat =
        buildRequest(Post(makeURL(::createChat), input), token)
            .send(httpClient) { it.handle() }

    @Route("/chat/{id}")
    override suspend fun sendMessage(token: String, input: MessageInput, chatId: Int) : Message =
        buildRequest(Post(makeURL(::sendMessage, chatId.toString()), input), token)
            .send(httpClient) { it.handle() }

}