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
    Chat(1, "Family", Timestamp.valueOf("2023-05-11 21:15:02.602668")),
    Chat(2, "Gym", Timestamp.valueOf("2023-05-11 21:15:02.602668")),
    Chat(3, "Friends", Timestamp.valueOf("2023-05-11 21:15:02.602668")),
    Chat(4, "Cousins", Timestamp.valueOf("2023-05-11 21:15:02.602668")),
    Chat(5, "Best Friend", Timestamp.valueOf("2023-05-11 21:15:02.602668"))
)