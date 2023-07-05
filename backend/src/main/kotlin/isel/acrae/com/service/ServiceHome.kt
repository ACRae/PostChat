package isel.acrae.com.service

import isel.acrae.com.domain.Token
import isel.acrae.com.domain.encodePassword
import isel.acrae.com.domain.generateBase64Token
import isel.acrae.com.domain.makePhoneNumber
import isel.acrae.com.http.controller.TOKEN_MAX_AGE
import isel.acrae.com.http.error.ApiIllegalArgumentException
import isel.acrae.com.http.error.ApiInternalErrorException
import isel.acrae.com.http.error.ApiResourceNotFoundException
import isel.acrae.com.http.error.ProblemTypeDetail
import isel.acrae.com.logger.logger
import isel.acrae.com.logger.runLogging
import isel.acrae.com.repository.TransactionManager
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant


@Service
class ServiceHome(
    private val tManager: TransactionManager
) {
    companion object {
        private val logger = logger<ServiceHome>()
    }

    /**
     * Creates a new User and associated token
     * @param [name] user's name
     * @param [number] user's phone number
     * @param [password] user's password
     * @param [maxAge] token maximum age
     * @return user's [Token]
     */
    fun signIn(
        name: String, number: String,
        region: Int, password: String,
        maxAge: Int = TOKEN_MAX_AGE,
    ): Token =
        logger.runLogging(::signIn) {
            tManager.run {
                Check.validPassword(password).validUserName(name)
                    .validPhoneNumber(number, region)

                val phoneNumber = makePhoneNumber(region, number)

                val user = it.repositoryUser.getUserInfo(phoneNumber)
                user.checkNull(ApiIllegalArgumentException(ProblemTypeDetail.USER_ALREADY_EXISTS))

                it.repositoryUser.createUser(
                    name, phoneNumber,
                    encodePassword(password, number, region),
                )
                val expires = Timestamp.from(Instant.now().plusSeconds(maxAge.toLong()))
                val token = generateBase64Token()
                it.repositoryUser.insertToken(phoneNumber, token, expires)
                Token(token, expires)
            }
        }

    /**
     * Login to the service and retrieve associated user's token
     * @param [number] user's phone number
     * @param [password] user's password
     * @param [region] user's phone number region
     * @param [maxAge] token maximum age
     */
    fun login(
        number: String, region: Int,
        password: String, maxAge: Int = TOKEN_MAX_AGE
    ): Token =
        logger.runLogging(::login) {
            tManager.run {
                Check.validPassword(password).validPhoneNumber(number, region)

                val user = it.repositoryUser.getUserFrom(
                    makePhoneNumber(region, number),
                    encodePassword(password, number, region)
                )

                user.checkNotNull(ApiResourceNotFoundException(ProblemTypeDetail.USER_NOT_FOUND))

                val token = it.repositoryUser.getToken(user.phoneNumber)
                token.checkNotNull(ApiInternalErrorException(ProblemTypeDetail.DEFAULT(null)))

                if (token.expires.time < System.currentTimeMillis()) {
                    val newToken = generateBase64Token()
                    val expires = Timestamp.from(Instant.now().plusSeconds(maxAge.toLong()))
                    it.repositoryUser.updateToken(user.phoneNumber, newToken, expires)
                    Token(newToken, expires)
                } else token
            }
        }
}