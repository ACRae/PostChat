package isel.acrae.postchat.service.web

import isel.acrae.postchat.domain.CreateUserInput
import isel.acrae.postchat.domain.LoginInput
import isel.acrae.postchat.service.HomeDataService
import okhttp3.OkHttpClient

class HomeDataWebService(
    baseUrl : String,
    private val httpClient: OkHttpClient,
) : HomeDataService, Web(baseUrl) {

    @Route("/register")
    override suspend fun register(userInput: CreateUserInput): String =
        buildRequest(Post(makeURL(::register), userInput))
            .send(httpClient) { it.handle() }

    @Route("/login")
    override suspend fun login(userInput: LoginInput): String =
        buildRequest(Post(makeURL(::login), userInput))
            .send(httpClient) { it.handle() }

    @Route("/logout")
    override suspend fun logout(token: String): String =
        buildRequest(Post(makeURL(::logout), null), token)
            .send(httpClient) { it.handle() }
}