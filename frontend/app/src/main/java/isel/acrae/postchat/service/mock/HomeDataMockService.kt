package isel.acrae.postchat.service.mock

import isel.acrae.postchat.domain.CreateUserInput
import isel.acrae.postchat.domain.LoginInput
import isel.acrae.postchat.domain.User
import isel.acrae.postchat.room.entity.UserEntity
import isel.acrae.postchat.service.HomeDataService
import isel.acrae.postchat.service.mock.data.makeToken
import isel.acrae.postchat.service.mock.data.mockTokens
import isel.acrae.postchat.service.mock.data.mockUsers
import isel.acrae.postchat.service.mock.data.mockUsersInfo

class HomeDataMockService : HomeDataService {
    override suspend fun register(userInput: CreateUserInput): String {
        val phoneNumber = userInput.region.toString() + userInput.number
        val user = User(phoneNumber, userInput.password, userInput.name, userInput.bio)
        val userInfo = UserEntity(phoneNumber, user.name, user.bio)
        val token = makeToken()
        mockTokens.plus(token to phoneNumber)
        mockUsers.plus(userInput.password to user)
        mockUsersInfo.add(userInfo)
        return token
    }

    override suspend fun login(userInput: LoginInput): String {
        val number = mockUsers[userInput.password]!!.phoneNumber
        assert(number == userInput.number)
        return mockTokens.toList().first { it.second == userInput.number }.first
    }

    override suspend fun logout(token: String): String = "INVALID_TOKEN"
}