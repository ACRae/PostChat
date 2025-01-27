package isel.acrae.postchat.service.mock

import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.domain.UserInfoList
import isel.acrae.postchat.service.UserDataService
import isel.acrae.postchat.service.mock.data.checkToken
import isel.acrae.postchat.service.mock.data.mockTokens
import isel.acrae.postchat.service.mock.data.mockUsers
import isel.acrae.postchat.service.mock.data.mockUsersInfo

class UserDataMockService : UserDataService {
    override suspend fun getUsers(token: String, users: List<String>): UserInfoList {
        checkToken(token)
        return UserInfoList(
            mockUsersInfo.dropWhile { users.contains(it.phoneNumber)  }
        )
    }

    override suspend fun getUser(token: String, phoneNumber: String): UserInfo {
        checkToken(token)
        return mockUsersInfo.first { it.phoneNumber == phoneNumber }
    }

    override suspend fun deleteUser(token: String) {
        checkToken(token)
        val id = mockTokens[token]
        mockUsers.remove(id)
        mockUsersInfo.removeIf { it.phoneNumber == id }
    }
}