package isel.acrae.postchat.service

import isel.acrae.postchat.domain.CreateUserInput
import isel.acrae.postchat.domain.LoginInput

interface HomeDataService {

    suspend fun register(userInput: CreateUserInput): String

    suspend fun login(userInput : LoginInput): String

    suspend fun logout(token: String): String
}