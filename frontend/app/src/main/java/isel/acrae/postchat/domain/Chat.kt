package isel.acrae.postchat.domain

import java.sql.Timestamp

data class MessageList(
    val list : List<Message>
) : Domain

data class Message(
    val id : Int,
    val userFrom : String,
    val chatTo : Int,
    val mergedContent : String,
    val handwrittenContent : String,
    val templateName: String,
    val createdMessageAt : Timestamp,
): Domain

data class Chat (
    val id : Int,
    val name: String?,
    val createdAt: Timestamp
): Domain


data class ChatInfo(
    val props: Chat,
    val usersInfo: List<UserInfo>
): Domain

data class CreateChatInput(
    val phoneNumbers : List<String>,
    val name : String?
): Domain

data class ChatList(
    val list : List<Chat>
): Domain


data class MessageInput(
    val content : String,
    val templateName: String,
    val createdAt: Timestamp,
): Domain

data class HandwrittenInput(
    val handwrittenContent : String
): Domain

