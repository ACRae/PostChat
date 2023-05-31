package isel.acrae.postchat.domain

import java.sql.Timestamp

data class MessageList(
    val list : List<Message>
)

data class Message(
    val id : Int,
    val userFrom : String,
    val chatTo : Int,
    val mergedContent : String,
    val handwrittenContent : String,
    val templateName: String,
    val createdMessageAt : Timestamp,
) {
    fun makeFileId() =
        createdMessageAt.toString().replace(Regex("[: .-]"), "") +
        templateName + id + chatTo +
        userFrom + ".svg"
}

data class Chat (
    val id : Int,
    val name: String,
    val createdAt: Timestamp
)


data class ChatInfo(
    val props: Chat,
    val usersInfo: List<UserInfo>
)

data class CreateChatInput(
    val phoneNumbers : List<String>,
    val name : String,
    val timestamp: Timestamp,
)

data class ChatList(
    val list : List<Chat>
)


data class MessageInput(
    val content : String,
    val templateName: String,
    val createdAt: Timestamp,
)

data class HandwrittenInput(
    val handwrittenContent : String
)

