package isel.acrae.postchat.service.mock.data

import isel.acrae.postchat.domain.UserInfo
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
    ),
    UserInfo(
        "351912345673",
        "Joane",
    ),
    UserInfo(
        "351912345674",
        "Luke",
    ),
    UserInfo(
        "351912345675",
        "Joe Biden",
    ),
    UserInfo(
        "351912345676",
        "Alice",
    ),
    UserInfo(
        "351912345677",
        "Mark",
    ),
    UserInfo(
        "351912345678",
        "Ramsey",
    ),
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






