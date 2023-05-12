package isel.acrae.com.service

import isel.acrae.com.domain.Chat
import isel.acrae.com.domain.MessageHolder
import isel.acrae.com.domain.Template
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
){
    companion object {
        fun from(m: MessageHolder, mergedContent: String) =
            Message(
                m.userFrom, m.chatTo, m.chatName,
                mergedContent, m.content, m.templateName,
                m.createdMessageAt
            )
    }
}

data class ChatList(
    val list : List<Chat>
)

data class TemplateList(
    val list : List<Template>
)