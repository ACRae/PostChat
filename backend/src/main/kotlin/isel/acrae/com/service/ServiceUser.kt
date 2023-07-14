package isel.acrae.com.service


import isel.acrae.com.domain.User
import isel.acrae.com.domain.UserInfo
import isel.acrae.com.domain.UserInfoList
import isel.acrae.com.http.error.ApiIllegalArgumentException
import isel.acrae.com.http.error.ProblemTypeDetail
import isel.acrae.com.logger.logger
import isel.acrae.com.logger.runLogging
import isel.acrae.com.repository.TransactionManager
import org.springframework.stereotype.Service

@Service
class ServiceUser(
    private val tManager: TransactionManager
) {
    companion object {
        private val logger = logger<ServiceUser>()
    }

    /**
     * Gets all users signed in the service. Requires users phone numbers.
     * @param [phoneNumberId] user phone number
     * @param [phoneNumbers] a list of phone numbers.
     * Phone numbers contain the country code and the number separated with a space ' '.
     * @return [UserInfoList] a list of [UserInfo]
     */
    fun getUsers(phoneNumberId: String, phoneNumbers: List<String>): UserInfoList =
        logger.runLogging(::getUsers) {
            tManager.run {
                if(phoneNumbers.isEmpty()) UserInfoList(emptyList())
                UserInfoList(
                    it.repositoryUser.getUsers(phoneNumberId, phoneNumbers)
                )
            }
        }


    /**
     * Get user info
     * @param [phoneNumber] phone number.
     * @return [UserInfo]
     */
    fun getUser(phoneNumber: String): UserInfo? =
        logger.runLogging(::getUsers) {
            tManager.run {
                it.repositoryUser.getUserInfo(phoneNumber)
            }
        }

    /**
     * Gets a user from a token
     * @param [token] user token
     */
    fun getUserFromToken(token: String): User =
        logger.runLogging(::getUserFromToken) {
            tManager.run {
                val user = it.repositoryUser.getUser(token)
                user.checkNotNull(
                    ApiIllegalArgumentException(
                        ProblemTypeDetail.INVALID_TOKEN
                    )
                )
                user
            }
        }

    /**
     * Deletes a user
     * @param [userPhoneNumber] user phone number
     */
    fun deleteUser(userPhoneNumber: String) : Unit =
        logger.runLogging(::deleteUser) {
            tManager.run {
                it.repositoryUser.deleteUserAndToken(userPhoneNumber)
            }
        }

}
