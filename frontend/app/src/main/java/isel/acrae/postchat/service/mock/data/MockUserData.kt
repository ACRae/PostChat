package isel.acrae.postchat.service.mock.data

import isel.acrae.postchat.room.entity.UserEntity
import java.util.UUID

fun makeToken() = UUID.randomUUID().toString()

val mockUsersInfo = mutableListOf(
    UserEntity(
        "351912345671",
        "John",
        "Living life"
    ),
    UserEntity(
        "351912345672",
        "Mary",
        "Having fun"
    )
)

val mockUsers = mutableMapOf(
    "PASS1" to UserEntity(
        "351912345671",
        "John",
        "Living life"
    ),
    "PASS2" to UserEntity(
        "351912345672",
        "Mary",
        "Having fun"
    )
)






