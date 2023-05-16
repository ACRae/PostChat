package isel.acrae.com.http.controller

import isel.acrae.com.domain.User
import isel.acrae.com.http.Routes
import isel.acrae.com.http.pipeline.Authenticate
import isel.acrae.com.service.MessageList
import isel.acrae.com.service.ServiceChat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Routes.Message.MESSAGE)
class ControllerMessage(
    private val service : ServiceChat
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getMessages(@Authenticate user: User) : MessageList =
        service.getMessages(user.phoneNumber)
}