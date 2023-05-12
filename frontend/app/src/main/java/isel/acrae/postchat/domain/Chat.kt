package isel.acrae.postchat.domain

import java.sql.Timestamp

data class MessageList(
    val list : List<Message>
)

data class Message(
    val userFrom : String,
    val chatTo : Int,
    val chatName: String?,
    val mergedContent : String,
    val handwrittenContent : String,
    val templateName: String,
    val createdMessageAt : Timestamp
)

data class Chat (
    val id : Int,
    val name: String?,
    val createdAt: Timestamp
)


data class ChatInfo(
    val props: Chat,
    val usersInfo: List<UserInfo>
)

data class CreateChatInput(
    val phoneNumbers : List<String>,
    val name : String?
)

data class MessageInput(
    val content : String,
    val templateName: String,
)

data class HandwrittenInput(
    val handwrittenContent : String
)

