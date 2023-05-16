package isel.acrae.postchat.domain


data class UserInfo(
    val phoneNumber: String,
    val name : String,
    val bio : String?
): Domain

data class UserInfoList(
    val list: List<UserInfo>
): Domain

data class CreateUserInput(
    val name: String,
    val number: String,
    val region: Int,
    val password: String,
    val bio: String?
): Domain

data class LoginInput(
    val number : String,
    val region : Int,
    val password : String,
): Domain
