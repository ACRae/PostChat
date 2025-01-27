package isel.acrae.postchat.service

import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.domain.UserInfoList

interface UserDataService {

    suspend fun getUsers(token: String, users: List<String>): UserInfoList

    suspend fun getUser(token: String, phoneNumber: String): UserInfo

    suspend fun deleteUser(token: String)
}