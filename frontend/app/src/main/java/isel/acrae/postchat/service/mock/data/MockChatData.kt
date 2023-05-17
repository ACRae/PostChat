package isel.acrae.postchat.service.mock.data

import isel.acrae.postchat.room.entity.ChatEntity
import java.sql.Timestamp

//UserId to ChatIds
val mockChatUserRelation = mutableMapOf(
    "351912345671" to listOf(1),
    "351912345672" to listOf(1),
)

//ChatId to Chat
val mockChats = mutableListOf(
    ChatEntity(1, "Test Chat", Timestamp(System.currentTimeMillis()).toString())
)