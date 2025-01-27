package isel.acrae.com.http.pipeline.interceptor

import isel.acrae.com.http.controller.RequestMediaType
import isel.acrae.com.http.error.ApiInvalidContentTypeException
import isel.acrae.com.http.error.ProblemTypeDetail
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor

@Component
class InterceptorContentType : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (handler is HandlerMethod && handler.hasMethodAnnotation(RequestMediaType::class.java)) {
            if (request.contentType != handler.getMethodAnnotation(RequestMediaType::class.java)!!.value) {
                throw ApiInvalidContentTypeException(ProblemTypeDetail.INVALID_CONTENT_TYPE)
            }
            return true
        }
        return true
    }
}