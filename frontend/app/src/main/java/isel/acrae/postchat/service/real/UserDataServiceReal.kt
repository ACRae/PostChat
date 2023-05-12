package isel.acrae.postchat.service.real

import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.domain.UserInfoList
import isel.acrae.postchat.service.UserDataService

class UserDataServiceReal : UserDataService {
    override suspend fun getUsers(token: String, users: List<String>): UserInfoList {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(token: String, phoneNumber: String): UserInfo {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(token: String) {
        TODO("Not yet implemented")
    }
}