package isel.acrae.com.http.controller

import isel.acrae.com.MockController
import isel.acrae.com.buildGet
import isel.acrae.com.buildPost
import isel.acrae.com.getCookie
import isel.acrae.com.http.Routes
import isel.acrae.com.http.input.CreateUserInput
import isel.acrae.com.http.input.LoginInput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class ControllerHomeTest : MockController() {

    private final val createUserInput = CreateUserInput.TEST
    val loginUserInput = LoginInput(
        createUserInput.number,
        createUserInput.region,
        createUserInput.password
    )

    val userUriPhone = fun(phone : String) =
        Routes.User.USER_PHONE_URI
            .replace("{phone}", phone)
    @Test
    fun register() {
        val response = webTestClient.buildPost(
            Routes.Home.REGISTER, createUserInput, HttpStatus.CREATED
        )
        val token = response.getCookie("token")

        webTestClient.buildGet(userUriPhone(createUserInput.number), HttpStatus.OK, token)
    }

    @Test
    fun login() {
        val created = webTestClient.buildPost(
            Routes.Home.REGISTER, createUserInput, HttpStatus.CREATED
        )
        val token1 = created.getCookie("token")

        val response = webTestClient.buildPost(
            Routes.Home.LOGIN, loginUserInput, HttpStatus.OK
        )
        val token2 = response.getCookie("token")
        assertEquals(token1?.value, token2?.value)
        webTestClient.buildGet(userUriPhone(createUserInput.number), HttpStatus.OK, token2)
    }

    @Test
    fun logout() {
        val response = webTestClient.buildPost(
            Routes.Home.REGISTER, createUserInput, HttpStatus.CREATED
        )
        val token = response.getCookie("token")

        val logoutRes = webTestClient.buildPost(
            Routes.Home.LOGOUT, null, HttpStatus.NO_CONTENT,
            token,
        )
        val tokenLogout = logoutRes.getCookie("token")
        assertEquals(tokenLogout?.value ,"")
        assert(logoutRes.responseBodyContent?.isEmpty() ?: true)
    }
}