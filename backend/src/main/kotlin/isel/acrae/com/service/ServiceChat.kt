package isel.acrae.com.service

import isel.acrae.com.domain.Chat
import isel.acrae.com.domain.ChatInfo
import isel.acrae.com.http.Routes
import isel.acrae.com.http.error.ApiIllegalArgumentException
import isel.acrae.com.http.error.ApiInternalErrorException
import isel.acrae.com.http.error.ProblemTypeDetail
import isel.acrae.com.logger.logger
import isel.acrae.com.logger.runLogging
import isel.acrae.com.repository.TransactionManager
import isel.acrae.com.svg.SvgProcessing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.sql.Timestamp
import java.time.Instant

@Service
class ServiceChat(
    private val tManager : TransactionManager
) {
    companion object{
        private val logger = logger<ServiceChat>()
    }

    /**
     * Gets all users messages. Svg's are merged into one.
     * @param [userPhone] user Id
     * @return [MessageList] a list of messages
     */
    fun getMessages(userPhone: String): MessageList =
        logger.runLogging(::getMessages) {
            tManager.run {
                MessageList(
                    it.repositoryChat.getMessages(userPhone).map {m->
                        val mergedContent = SvgProcessing.mergeBase64(m.backplate, m.content)
                        Message.from(m, mergedContent)
                    }
                )
            }
        }


    /**
     * Gets all users chats
     * @param [userPhoneNumber] user id
     * @return [ChatList] a list of chats
     */
    fun getChats(userPhoneNumber: String): ChatList =
        logger.runLogging(::getChats) {
            tManager.run {
                ChatList(it.repositoryChat.getUserChats(userPhoneNumber))
            }
        }

    /**
     * Get chat info
     * @param [userPhoneNumber] user id
     * @param [chatId] chat id
     * @return [ChatInfo] chat additional information
     */
    fun getChatInfo(userPhoneNumber: String, chatId: Int): ChatInfo =
        logger.runLogging(::getChatInfo) {
            tManager.run {
                val chat = it.repositoryChat.getChat(chatId, userPhoneNumber)
                chat.checkNotNull(ApiIllegalArgumentException(ProblemTypeDetail.CHAT_NOT_FOUND))
                ChatInfo(
                    chat, it.repositoryChat.getChatMembers(chatId, userPhoneNumber)
                )
            }
        }

    /**
     * Extract text from a handwritten message
     * @param [content] base64 svg containing the handwritten message
     * @return [String] the extracted text
     */
    fun ocrMessage(content: String, emitter: SseEmitter) : SseEmitter =
        logger.runLogging(::getChatInfo) {
            CoroutineScope(Dispatchers.IO).launch {
                flow {
                    emit(SvgProcessing.ocr(content))
                }.collect {
                    logger.info(it)
                    emitter.send(it)
                }
                emitter.complete()
            }
            emitter
        }

    /**
     * Create a new chat.
     * If the [phoneNumbers] list contains only 1 phone number, than it creates a private chat.
     * If the [phoneNumbers] list contains more than 1 phone number, than it creates a group chat.
     * A private chat has obligatory a null [name]
     * @param [userPhoneNumber] user id
     * @param [name] name of the chat
     * @param [phoneNumbers] list of phone numbers.
     * A phone number includes the country code and the phone number itself seperated by a space ' '.
     * @return [ChatInfo] the created chat
     */
    fun createChat(userPhoneNumber: String, phoneNumbers: List<String>, name: String?, timestamp: Timestamp): Chat =
        logger.runLogging(::createChat) {
            tManager.run {
                val repoUser = it.repositoryUser
                val repoChat = it.repositoryChat
                val users = repoUser.getUsers(userPhoneNumber, phoneNumbers)

                if (users.size == 1) {
                    //force name to be null when it's a private chat
                    name.checkNull(ApiIllegalArgumentException(ProblemTypeDetail.INVALID_NAME))
                    repoChat.getPrivateChat(userPhoneNumber, users.first().phoneNumber)
                        .checkNull(ApiIllegalArgumentException(ProblemTypeDetail.PRIVATE_CHAT_ALREADY_FOUND))
                }else {
                    name.checkNotNull(ApiIllegalArgumentException(ProblemTypeDetail.REQUIRES_NAME))
                }

                val chatId = repoChat.createChat(name, timestamp)
                chatId.checkNotNull(ApiInternalErrorException(ProblemTypeDetail.DEFAULT(null)))
                repoChat.insertChatMember(userPhoneNumber, chatId)
                users.forEach { user ->
                    repoChat.insertChatMember(user.phoneNumber, chatId)
                }
                repoChat.getChat(chatId, userPhoneNumber)!!
            }
        }


    /**
     * Sends a message to a specific chat
     * @param [chatId] chat id
     * @param [phoneNumber] user id
     * @param [content] message content
     * @param [templateName] template name
     */
    fun sendMessage(
        phoneNumber: String, chatId: Int,
        content: String, templateName: String,
        timestamp: Timestamp,
    ): Message = logger.runLogging(::sendMessage) {
            tManager.run {
                val template = it.repositoryTemplate.getTemplate(templateName)
                template.checkNotNull(ApiIllegalArgumentException(ProblemTypeDetail.TEMPLATE_NOT_FOUND))

                val chat = it.repositoryChat.getChat(chatId, phoneNumber)
                chat.checkNotNull(ApiIllegalArgumentException(ProblemTypeDetail.CHAT_NOT_FOUND))

                val id = it.repositoryChat.sendMessage(phoneNumber, content, templateName, chatId, timestamp)
                id.checkNotNull(ApiIllegalArgumentException(ProblemTypeDetail.DEFAULT(null)))

                val mergedContent = SvgProcessing.mergeBase64(template.content, content)

                Message(id, phoneNumber, chatId, mergedContent, content, templateName, timestamp)
            }
        }
}