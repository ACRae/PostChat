package isel.acrae.postchat.service.mock.data

val mockTokens = mutableMapOf(
    "TEST_TOKEN1" to "351912345671",
    "TEST_TOKEN2" to "351912345672"
)

fun checkToken(s : String) {
    if(!mockTokens.keys.contains(s))
        throw Exception()
}