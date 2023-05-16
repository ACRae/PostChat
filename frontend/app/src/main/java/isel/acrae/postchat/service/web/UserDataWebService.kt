package isel.acrae.postchat.service.web

import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.domain.UserInfoList
import isel.acrae.postchat.room.dao.UserDao
import isel.acrae.postchat.room.entity.UserEntity
import isel.acrae.postchat.service.UserDataService
import isel.acrae.postchat.service.web.mapper.EntityMapper
import isel.acrae.postchat.service.web.mapper.roomHandle
import okhttp3.OkHttpClient

class UserDataWebService(
    private val userDao: UserDao,
    baseUrl : String,
    private val httpClient: OkHttpClient,
) : UserDataService, Web(baseUrl) {

    @Route("/user")
    override suspend fun getUsers(token: String, users: List<String>): List<UserEntity> =
        buildRequest(Get(makeURL(::getUsers)
            .addQuery(QueryParam.from("phoneNumbers", users))), token)
            .send<UserInfoList>(httpClient) { it.handle() }
            .roomHandle(userDao) {
                insertAll(EntityMapper.from(it.list))
                getAll()
            }

    @Route("/user/{number}")
    override suspend fun getUser(token: String, phoneNumber: String): UserInfo =
        buildRequest(Get(makeURL(::getUser, phoneNumber)), token)
            .send(httpClient) { it.handle() }

    @Route("/user")
    override suspend fun deleteUser(token: String) : Unit =
        buildRequest(Delete(makeURL(::deleteUser)), token)
            .send(httpClient) { it.handle() }
}