package isel.acrae.com.repository.jdbi


import isel.acrae.com.domain.*
import isel.acrae.com.repository.RepositoryUser
import org.jdbi.v3.core.Handle
import java.sql.Timestamp


class RepositoryUserImpl(private val handle: Handle) : RepositoryUser {

    override fun createUser(
        name: String, phoneNumber: String,
        passwordValidator: String, bio: String?
    ): Int =
        handle.createUpdate(
            """
            insert into _user(phone_number, password_validator, name, bio)
            values(:phoneNumber, :password_validator, :name, :bio)
            """.trimIndent()
        )
            .bind("password_validator", passwordValidator)
            .bind("name", name)
            .bind("bio", bio)
            .bind("phoneNumber", phoneNumber)
            .execute()

    override fun insertToken(phoneNumber: String, token: String, expires: Timestamp) {
        handle.createUpdate(
            """
                insert into user_token(token, user_id, expires_at)
                values(:token, :phoneNumber, :expires)
            """.trimIndent()
        )
            .bind("token", token)
            .bind("phoneNumber", phoneNumber)
            .bind("expires", expires)
            .execute()
    }

    override fun getUser(token: String): User? =
        handle.createQuery(
            """
                select phone_number, password_validator, name, bio 
                from user_token join _user ui on ui.phone_number = user_token.user_id
                where token = :token
            """.trimIndent()
        )
            .bind("token", token)
            .map(UserMapper())
            .firstOrNull()

    override fun getUserFrom(phoneNumber: String, passwordValidator: String): UserInfo? =
        handle.createQuery(
            """
                select phone_number, name, bio from _user
                where phone_number = :phoneNumber and password_validator = :passwordValidator
            """.trimIndent()
        )
            .bind("phoneNumber", phoneNumber)
            .bind("passwordValidator", passwordValidator)
            .map(UserInfoMapper())
            .firstOrNull()


    override fun getToken(phoneNumber: String): Token? =
        handle.createQuery(
            """
            select token, expires_at from user_token 
            join _user u on u.phone_number = user_token.user_id 
            where phone_number = :phoneNumber
            """.trimIndent()
        )
            .bind("phoneNumber", phoneNumber)
            .map(TokenMapper())
            .firstOrNull()

    override fun updateToken(phoneNumber: String, newToken: String, newExpire: Timestamp) {
        handle.createUpdate(
            """
                update user_token set token = :newToken,
                expires_at = :newExpire
                where user_id = :phoneNumber
            """.trimIndent()
        )
            .bind("phoneNumber", phoneNumber)
            .bind("newToken", newToken)
            .bind("newExpire", newExpire)
            .execute()

    }

    override fun getUserInfo(phoneNumber: String): UserInfo? =
        handle.createQuery(
            """
                select phone_number, name, bio from _user 
                where phone_number = :phoneNumber 
            """.trimIndent()
        )
            .bind("phoneNumber", phoneNumber)
            .map(UserInfoMapper())
            .firstOrNull()


    override fun getUsers(phoneNumber: String, phoneNumberList: List<String>): List<UserInfo> =
        handle.createQuery(
            """
            select phone_number, name, bio from _user
            where phone_number 
            in (${phoneNumberList.joinToString { "'$it'" }})
            and phone_number != :phoneNumber
            """.trimIndent()
        )
            .bind("phoneNumber", phoneNumber)
            .map(UserInfoMapper())
            .list()


    override fun deleteUserAndToken(phoneNumber: String) {
        handle.createUpdate(
            """
            delete from user_token where user_id = :phoneNumber;
            delete from _user where phone_number = :phoneNumber
            """.trimIndent()
        )
            .bind("phoneNumber", phoneNumber)
            .execute()
    }

}