package isel.acrae.postchat.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


data class UserInfo @JsonCreator constructor(
    @JsonProperty("phoneNumber") val phoneNumber: String,
    @JsonProperty("name") val name : String,
)

data class UserInfoList @JsonCreator constructor(
    @JsonProperty("list") val list: List<UserInfo>
)

data class CreateUserInput @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("number") val number: String,
    @JsonProperty("region") val region: Int,
    @JsonProperty("password") val password: String,
)

data class LoginInput @JsonCreator constructor(
    @JsonProperty("number") val number : String,
    @JsonProperty("region") val region : Int,
    @JsonProperty("password") val password : String,
)

data class User @JsonCreator constructor(
    @JsonProperty("phoneNumber") val phoneNumber: String,
    @JsonProperty("passwordValidator") val passwordValidator: String,
    @JsonProperty("name") val name : String
)