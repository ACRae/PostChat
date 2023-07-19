package isel.acrae.com.repository

import isel.acrae.com.domain.Chat
import isel.acrae.com.domain.MessageHolder
import isel.acrae.com.domain.UserInfo
import java.sql.Timestamp


/**
 * Chat Database operations
 */
interface RepositoryChat {
    fun getMessages(phoneNumber: String): List<MessageHolder>
    fun sendMessage(
        userFromPhone: String,
        content: String,
        templateName: String,
        chatId: Int,
        timestamp: Timestamp,
        userTo: String
    ): Int?

    fun getChat(chatId: Int, phoneNumber: String): Chat?
    fun getUserChats(phoneNumber: String): List<Chat>
    fun getChatMembers(chatId: Int, phoneNumber: String): List<UserInfo>
    fun getPrivateChat(phoneNumber1: String, phoneNumber2: String): Chat?
    fun createChat(name: String?, timestamp: Timestamp): Int?
    fun insertChatMember(phoneNumber: String, chatId: Int)
}