package isel.acrae.postchat.domain


data class UserInfo(
    val phoneNumber: String,
    val name : String,
)

data class UserInfoList(
    val list: List<UserInfo>
)

data class CreateUserInput(
    val name: String,
    val number: String,
    val region: Int,
    val password: String,
)

data class LoginInput(
    val number : String,
    val region : Int,
    val password : String,
)

data class User(
    val phoneNumber: String,
    val passwordValidator: String,
    val name : String
)