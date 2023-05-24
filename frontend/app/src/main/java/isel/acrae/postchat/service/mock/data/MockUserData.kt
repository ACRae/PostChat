package isel.acrae.postchat.service.mock.data

import isel.acrae.postchat.domain.User
import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.room.entity.UserEntity
import java.util.UUID

fun makeToken() = UUID.randomUUID().toString()

val mockUsersInfo = mutableListOf(
    UserInfo(
        "351912345671",
        "John",
    ),
    UserInfo(
        "351912345672",
        "Mary",
    )
)

val mockUsers = mutableMapOf(
    "PASS1" to UserInfo(
        "351912345671",
        "John",
    ),
    "PASS2" to UserInfo(
        "351912345672",
        "Mary",
    )
)






