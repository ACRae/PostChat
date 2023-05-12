package isel.acrae.postchat.service

import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.domain.UserInfoList

interface UserDataService {
    @Route("/user")
    suspend fun getUsers(token: String, users: List<String>): UserInfoList

    @Route("/user/{number}")
    suspend fun getUser(token: String, phoneNumber: String): UserInfo

    @Route("/user")
    suspend fun deleteUser(token: String)
}