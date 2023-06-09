package isel.acrae.com.http.pipeline.interceptor

import isel.acrae.com.domain.canBeToken
import isel.acrae.com.http.pipeline.Authenticate
import isel.acrae.com.http.pipeline.resolver.UserIdArgumentResolver
import isel.acrae.com.logger.logger
import isel.acrae.com.service.ServiceUser
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor



@Component
class InterceptorAuthentication(
    val service : ServiceUser,
    val headerProcessor: AuthHeaderProcess
) : HandlerInterceptor {
    companion object {
        private val logger = logger<InterceptorAuthentication>()
    }
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if(handler is HandlerMethod && handler.methodParameters.any {
                it.hasParameterAnnotation(Authenticate::class.java)
            }
        ) {
            val tokenCookie = request.cookies?.first{ it.name == "token" }
            if(tokenCookie != null) {
                tokenCookie.value.let {
                    if (it != null && it.canBeToken()) {
                        UserIdArgumentResolver.addUserIdTo(
                            service.getUserFromToken(it),
                            request
                        )
                        return true.also { logger.info("Request: ${request.method} ${request.requestURI} - Authorized") }
                    }
                }
            }else {
                val user = headerProcessor.process(request)
                if(user != null) {
                    UserIdArgumentResolver.addUserIdTo(user,request)
                    return true.also { logger.info("Request: ${request.method} ${request.requestURI} - Authorized") }
                }
            }

            response.status = HttpStatus.UNAUTHORIZED.value()
            return false.also { logger.info("Request: ${request.method} ${request.requestURI} - Unauthorized") }
        }
        return true
    }

}