package isel.acrae.com.http.controller

import isel.acrae.com.domain.User
import isel.acrae.com.http.Routes
import isel.acrae.com.http.input.HandwrittenInput
import isel.acrae.com.http.pipeline.Authenticate
import isel.acrae.com.service.HtrResult
import isel.acrae.com.service.ServiceChat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
@RequestMapping(Routes.Htr.HTR)
class ControllerHTR(
    private val service: ServiceChat
) {
    @PostMapping
    @RequestMediaType(MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    fun htrMessage(
        @Authenticate user: User,
        @RequestBody input: HandwrittenInput
    ) : HtrResult = service.htrMessage(input.handwrittenContent)
}