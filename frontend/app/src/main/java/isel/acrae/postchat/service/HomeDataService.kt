package isel.acrae.postchat.service

import isel.acrae.postchat.domain.CreateUserInput
import isel.acrae.postchat.domain.LoginInput

interface HomeDataService {

    @Route("/register")
    suspend fun register(userInput: CreateUserInput): String

    @Route("/login")
    suspend fun login(userInput : LoginInput): String

    @Route("/logout")
    suspend fun logout(token: String): String
}