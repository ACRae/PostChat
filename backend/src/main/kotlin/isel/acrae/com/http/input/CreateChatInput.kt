package isel.acrae.com.http.input

import java.sql.Timestamp

data class CreateChatInput(
    val phoneNumbers : List<String>,
    val name : String?,
    val createdAt : Timestamp,
)
