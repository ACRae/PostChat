package isel.acrae.com.http.input

data class CreateUserInput(
    val name: String,
    val number: String,
    val region: Int,
    val password: String,
) {
    companion object {
        fun generate(i : Int = 1) =
            CreateUserInput(
                "TestUser$i", (912912912 + i).toString(), 351,
                "Password$i"
            )

        val TEST = CreateUserInput(
            "TestUser", "912912912", 351,
            "Password1234"
        )
    }
}
