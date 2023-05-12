package isel.acrae.postchat.domain


data class UserInfo(
    val phoneNumber: String,
    val name : String,
    val bio : String?
)

data class UserInfoList(
    val list: List<UserInfo>
)

data class CreateUserInput(
    val name: String,
    val number: String,
    val region: Int,
    val password: String,
    val bio: String?
)

data class LoginInput(
    val number : String,
    val region : Int,
    val password : String,
)
