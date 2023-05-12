package isel.acrae.com.http.pipeline.interceptor

import isel.acrae.com.domain.User
import isel.acrae.com.domain.canBeToken
import isel.acrae.com.logger.logger
import isel.acrae.com.service.ServiceUser
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class AuthHeaderProcess(
    private val service: ServiceUser
) {

    companion object {
        private val logger = logger<AuthHeaderProcess>()
        private const val SCHEME = "Bearer"
        private const val AUTHORIZATION = "Authorization"
    }

    fun process(request: HttpServletRequest): User? {
        return try {
            val value = request.getHeader(AUTHORIZATION)
            val token = value.trim().split(" ").also {
                assert(
                    it.size == 2 &&
                    it[0] == SCHEME &&
                    it[1].canBeToken()
                )
            }[1]
            service.getUserFromToken(token)
        } catch (ex : Exception) {
            null
        }
    }
}