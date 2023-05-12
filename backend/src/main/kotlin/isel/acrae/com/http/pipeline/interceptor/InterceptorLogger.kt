package isel.acrae.com.http.pipeline.interceptor

import isel.acrae.com.logger.logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class InterceptorLogger : HandlerInterceptor {
    companion object {
        private val logger = logger<InterceptorLogger>()
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod) {
            logger.info("Request: ${request.method} ${request.requestURI} ${response.status}")
            return true
        }
        response.status = HttpStatus.NOT_FOUND.value()
        return false
    }
}