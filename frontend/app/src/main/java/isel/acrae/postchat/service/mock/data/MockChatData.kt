package isel.acrae.postchat.service.mock.data

import isel.acrae.postchat.domain.Chat
import java.sql.Timestamp

//UserId to ChatIds
val mockChatUserRelation = mutableMapOf(
    "351912345671" to listOf(1,2,3,4,5,6,7,8,9,10),
    "351912345672" to listOf(1),
)

//ChatId to Chat
val mockChats = mutableListOf(
    Chat(1, "Test Chat1", Timestamp(System.currentTimeMillis())),
    Chat(2, "Test Chat2", Timestamp(System.currentTimeMillis())),
    Chat(3, "Test Chat3", Timestamp(System.currentTimeMillis())),
    Chat(4, "Test Chat4", Timestamp(System.currentTimeMillis())),
    Chat(5, "Test Chat5", Timestamp(System.currentTimeMillis())),
    Chat(6, "Test Chat6", Timestamp(System.currentTimeMillis())),
    Chat(7, "Test Chat7", Timestamp(System.currentTimeMillis())),
    Chat(8, "Test Chat8", Timestamp(System.currentTimeMillis())),
    Chat(9, "Test Chat9", Timestamp(System.currentTimeMillis())),
    Chat(10, "Test Chat10", Timestamp(System.currentTimeMillis()))
)