package isel.acrae.com.http.input

import java.sql.Timestamp


data class MessageInput(
    val content : String,
    val templateName: String,
    val createdAt: Timestamp
)


data class HandwrittenInput(
    val handwrittenContent : String
)
