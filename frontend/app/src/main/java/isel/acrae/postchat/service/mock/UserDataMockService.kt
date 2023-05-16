package isel.acrae.postchat.service.mock

import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.domain.UserInfoList
import isel.acrae.postchat.service.UserDataService

class UserDataMockService : UserDataService {
    override suspend fun getUsers(token: String, users: List<String>): List<UserInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(token: String, phoneNumber: String): UserInfo {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(token: String) {
        TODO("Not yet implemented")
    }
}