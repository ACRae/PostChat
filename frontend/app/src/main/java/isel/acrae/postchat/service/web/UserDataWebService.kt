package isel.acrae.postchat.service.web

import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.domain.UserInfoList
import isel.acrae.postchat.service.UserDataService
import okhttp3.OkHttpClient

class UserDataWebService(
    baseUrl : String,
    private val httpClient: OkHttpClient,
) : UserDataService, Web(baseUrl) {

    @Route("/user")
    override suspend fun getUsers(token: String, users: List<String>): UserInfoList =
        buildRequest(Get(makeURL(::getUsers)
            .addQuery(QueryParam.from("phoneNumbers", users))), token)
            .send(httpClient) { it.handle() }

    @Route("/user/{number}")
    override suspend fun getUser(token: String, phoneNumber: String): UserInfo =
        buildRequest(Get(makeURL(::getUser, phoneNumber)), token)
            .send(httpClient) { it.handle() }

    @Route("/user")
    override suspend fun deleteUser(token: String) : Unit =
        buildRequest(Delete(makeURL(::deleteUser)), token)
            .send(httpClient) { it.handle() }
}