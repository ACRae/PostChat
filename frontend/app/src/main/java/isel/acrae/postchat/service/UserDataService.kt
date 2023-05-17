package isel.acrae.postchat.service

import isel.acrae.postchat.room.entity.UserEntity

interface UserDataService {

    suspend fun getUsers(token: String, users: List<String>): List<UserEntity>

    suspend fun getUser(token: String, phoneNumber: String): UserEntity

    suspend fun deleteUser(token: String)
}