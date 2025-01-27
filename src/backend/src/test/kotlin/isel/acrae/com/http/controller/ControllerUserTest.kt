package isel.acrae.com.http.controller

import isel.acrae.com.MockController
import isel.acrae.com.buildGet
import isel.acrae.com.domain.UserInfoList
import isel.acrae.com.http.Routes
import isel.acrae.com.mapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder

class ControllerUserTest : MockController() {
    val userUriPhone = fun(phone : String) =
        Routes.User.USER_PHONE_URI
            .replace("{phone}", phone)

    @Test
    fun getUsers() {
        val userTokenCookie = registerUser()
        val usersNumbers = registerUsers(4).map { it.second }
        val response = webTestClient.buildGet(
            UriComponentsBuilder.fromPath(
                Routes.User.USER
            ).queryParam(
                "phoneNumbers",
                usersNumbers.joinToString()
            )
                .build()
                .toUriString(),
            HttpStatus.OK,
            userTokenCookie
        )
        val users = mapper.readValue(
            response.responseBodyContent, UserInfoList::class.java
        )
        assertEquals(users.list.size, 4)
    }

    @Test
    fun me() {
        /*
        val userToken = registerUser()
        val response = webTestClient.buildGet(
            userUriPhone(CreateUserInput.TEST.phoneNumber), HttpStatus.OK,
            userToken
        )
        val user = mapper.readValue(
            response.responseBodyContent, UserInfo::class.java
        )
        assertEquals(user.name, CreateUserInput.TEST.name)
         */
    }
}