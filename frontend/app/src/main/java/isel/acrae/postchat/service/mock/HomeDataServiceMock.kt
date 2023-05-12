package isel.acrae.postchat.service.mock

import isel.acrae.postchat.domain.CreateUserInput
import isel.acrae.postchat.domain.LoginInput
import isel.acrae.postchat.service.HomeDataService

class HomeDataServiceMock : HomeDataService {
    override suspend fun register(userInput: CreateUserInput): String {
        TODO("Not yet implemented")
    }

    override suspend fun login(userInput: LoginInput): String {
        TODO("Not yet implemented")
    }

    override suspend fun logout(token: String): String {
        TODO("Not yet implemented")
    }
}