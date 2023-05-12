package isel.acrae.com.http.input


data class MessageInput(
    val content : String,
    val templateName: String,
)


data class HandwrittenInput(
    val handwrittenContent : String
)
