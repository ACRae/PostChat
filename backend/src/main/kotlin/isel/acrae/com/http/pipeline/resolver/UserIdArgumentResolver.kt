package isel.acrae.com.http.pipeline.resolver

import isel.acrae.com.domain.User
import isel.acrae.com.http.pipeline.Authenticate
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserIdArgumentResolver : HandlerMethodArgumentResolver{
    companion object {
        private val KEY = this::class.simpleName

        fun addUserIdTo(user: User, request: HttpServletRequest) {
            return request.setAttribute(KEY, user)
        }

        fun getUserIdFrom(request: HttpServletRequest): User? {
            return request.getAttribute(KEY) as User?
        }
    }

    override fun supportsParameter(parameter: MethodParameter) =
        parameter.hasParameterAnnotation(Authenticate::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java)!!
        return getUserIdFrom(request) ?: throw IllegalStateException(
            "There was an internal error while getting user id"
        )
    }

}