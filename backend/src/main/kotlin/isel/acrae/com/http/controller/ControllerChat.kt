package isel.acrae.com.http.controller

import isel.acrae.com.domain.Chat
import isel.acrae.com.domain.ChatInfo
import isel.acrae.com.domain.User
import isel.acrae.com.http.Routes
import isel.acrae.com.http.input.CreateChatInput
import isel.acrae.com.http.input.MessageInput
import isel.acrae.com.http.pipeline.Authenticate
import isel.acrae.com.service.ChatList
import isel.acrae.com.service.Message
import isel.acrae.com.service.MessageList
import isel.acrae.com.service.ServiceChat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Routes.Chat.CHAT)
class ControllerChat(
    private val service : ServiceChat
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getChats(@Authenticate user: User) : ChatList =
        service.getChats(user.phoneNumber)

    @PostMapping
    @RequestMediaType(MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    fun createChat(
        @Authenticate user: User,
        @RequestBody input: CreateChatInput,
    ) : Chat = service.createChat(user.phoneNumber, input.phoneNumbers, input.name)


    @PostMapping(Routes.Chat.CHAT_ID)
    @RequestMediaType(MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    fun sendMessage(
        @Authenticate user: User,
        @RequestBody input: MessageInput,
        @PathVariable id: Int,
    ) : Message = service.sendMessage(
            user.phoneNumber, id,
            input.content, input.templateName,
            input.createdAt
        )

    @GetMapping(Routes.Chat.CHAT_ID)
    @RequestMediaType(MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    fun getChatInfo(
        @Authenticate user: User,
        @PathVariable id: Int,
    ) : ChatInfo = service.getChatInfo(user.phoneNumber, id)
}