package isel.acrae.com.http.controller

import isel.acrae.com.domain.User
import isel.acrae.com.http.Routes
import isel.acrae.com.http.input.CreateUserInput
import isel.acrae.com.http.input.LoginInput
import isel.acrae.com.http.pipeline.Authenticate
import isel.acrae.com.service.ServiceHome
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI


@RestController
class ControllerHome(
    private val service : ServiceHome,
) {

    @PostMapping(Routes.Home.REGISTER)
    @RequestMediaType(MediaType.APPLICATION_JSON_VALUE)
    fun register(
        @RequestBody user : CreateUserInput,
        response: HttpServletResponse
    ) : ResponseEntity<*> {
        val token = service.signIn(
            user.name, user.number, user.region,
            user.password, user.bio, TOKEN_MAX_AGE
        )
        response.addCookie(buildCookie(token.content))
        return ResponseEntity.status(HttpStatus.CREATED).headers {
            it.location = URI(Routes.User.USER_PHONE_URI.replace(
                "{phone}", user.number)
            )
        }.build<Unit>()
    }

    @PostMapping(Routes.Home.LOGIN)
    @RequestMediaType(MediaType.APPLICATION_JSON_VALUE)
    fun login(
        @RequestBody input : LoginInput,
        response: HttpServletResponse
    ) : ResponseEntity<*> {
        val token = service.login(
            input.number, input.region, input.password, TOKEN_MAX_AGE
        )
        response.addCookie(buildCookie(token.content))

        return ResponseEntity.status(HttpStatus.OK).headers {
            it.location = URI(Routes.User.USER_PHONE_URI.replace(
                "{phone}", input.number)
            )
        }.build<Unit>()
    }

    @PostMapping(Routes.Home.LOGOUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(
        @Authenticate user : User,
        response: HttpServletResponse
    ) {
        val cookie = buildCookie(null, 0)
        response.addCookie(cookie)
    }
}
