package isel.acrae.postchat.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.sql.Timestamp

data class MessageList @JsonCreator constructor (
    @JsonProperty("list") val list : List<Message>
)

data class Message @JsonCreator constructor (
    @JsonProperty("id") val id : Int,
    @JsonProperty("userFrom") val userFrom : String,
    @JsonProperty("chatTo") val chatTo : Int,
    @JsonProperty("mergedContent") val mergedContent : String,
    @JsonProperty("handwrittenContent") val handwrittenContent : String,
    @JsonProperty("templateName") val templateName: String,
    @JsonProperty("createdMessageAt") val createdMessageAt : Timestamp,
) {
    fun makeFileId() =
        createdMessageAt.toString().replace(Regex("[: .-]"), "") +
        templateName + id + chatTo +
        userFrom + ".svg"
}

data class Chat @JsonCreator constructor (
    @JsonProperty("id") val id : Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("createdAt") val createdAt: Timestamp,
    @JsonProperty("lastMessage") val lastMessage: Timestamp? = null,
)

data class ChatInfo @JsonCreator constructor(
    @JsonProperty("props") val props: Chat,
    @JsonProperty("usersInfo") val usersInfo: List<UserInfo>
)

data class CreateChatInput @JsonCreator constructor(
    @JsonProperty("phoneNumbers") val phoneNumbers : List<String>,
    @JsonProperty("name") val name : String,
    @JsonProperty("timestamp") val timestamp: Timestamp,
)

data class ChatList @JsonCreator constructor(
    @JsonProperty("list") val list : List<Chat>
)


data class MessageInput @JsonCreator constructor(
    @JsonProperty("content") val content : String,
    @JsonProperty("templateName") val templateName: String,
    @JsonProperty("createdAt") val createdAt: Timestamp,
)

data class HandwrittenInput @JsonCreator constructor(
    @JsonProperty("handwrittenContent") val handwrittenContent : String
)

data class HtrResult @JsonCreator constructor(
    @JsonProperty("text") val text: String
)
