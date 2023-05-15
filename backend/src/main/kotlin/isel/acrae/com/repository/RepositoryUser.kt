package isel.acrae.com.repository

import isel.acrae.com.domain.Token
import isel.acrae.com.domain.User
import isel.acrae.com.domain.UserInfo
import java.sql.Timestamp


/**
 * User Database operations
 */
interface RepositoryUser {
    fun createUser(
        name: String, phoneNumber: String,
        passwordValidator: String, bio: String?
    ): Int

    fun insertToken(phoneNumber: String, token: String, expires: Timestamp)
    fun getUser(token: String): User?
    fun getToken(phoneNumber: String): Token?
    fun updateToken(phoneNumber: String, newToken: String, newExpire: Timestamp)
    fun getUserInfo(phoneNumber: String): UserInfo?
    fun getUserFrom(phoneNumber: String, passwordValidator: String): UserInfo?
    fun getUsers(phoneNumber: String, phoneNumberList: List<String>): List<UserInfo>
    fun deleteUserAndToken(phoneNumber: String)
}
