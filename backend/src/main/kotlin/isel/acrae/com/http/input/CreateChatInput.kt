package isel.acrae.com.http.input

data class CreateChatInput(
    val phoneNumbers : List<String>,
    val name : String?
)
