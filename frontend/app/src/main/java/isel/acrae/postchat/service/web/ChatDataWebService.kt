package isel.acrae.postchat.service.web

import isel.acrae.postchat.domain.ChatInfo
import isel.acrae.postchat.domain.ChatList
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.domain.Message
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.domain.MessageList
import isel.acrae.postchat.room.dao.ChatDao
import isel.acrae.postchat.room.dao.MessageDao
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.service.ChatDataService
import isel.acrae.postchat.service.web.mapper.EntityMapper
import isel.acrae.postchat.service.web.mapper.roomHandle
import okhttp3.OkHttpClient

class ChatDataWebService(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    baseUrl: String,
    private val httpClient: OkHttpClient,
) : ChatDataService, Web(baseUrl) {

    @Route("/chat")
    override suspend fun getChats(token: String): List<ChatEntity> =
        buildRequest(Get(makeURL(::getChats)), token)
            .send<ChatList>(httpClient) { it.handle() }
            .roomHandle(chatDao) {
                insertAll(EntityMapper.from(it.list))
                getAll()
            }

    @Route("/message")
    override suspend fun getMessages(token : String): List<MessageEntity> =
        buildRequest(Get(makeURL(::getMessages)), token)
            .send<MessageList>(httpClient) { it.handle() }
            .roomHandle(messageDao) {
                insertAll(EntityMapper.from(it.list))
                getAll()
            }

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
    override suspend fun sendMessage(token: String, input: MessageInput, chatId: Int) : MessageEntity =
        buildRequest(Post(makeURL(::sendMessage, chatId.toString()), input), token)
            .send<Message>(httpClient) { it.handle() }
            .roomHandle(messageDao){
                insert(EntityMapper.from(it))
                get(it.id)
            }

}