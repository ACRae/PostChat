package isel.acrae.com.http.controller

import isel.acrae.com.domain.User
import isel.acrae.com.domain.UserInfo
import isel.acrae.com.http.Routes
import isel.acrae.com.http.pipeline.Authenticate
import isel.acrae.com.service.ServiceUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Routes.User.USER)
class ControllerUser(
    private val service: ServiceUser
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getUsers(
        @Authenticate user: User,
        @RequestParam("phoneNumbers") phoneNumbers: List<String>
    ) = service.getUsers(user.phoneNumber, phoneNumbers)

    @DeleteMapping
    fun delete(@Authenticate user: User) =
        service.deleteUser(user.phoneNumber)

    @GetMapping(Routes.User.USER_PHONE)
    @ResponseStatus(HttpStatus.OK)
    fun getUser(
        @Authenticate user: User,
        @PathVariable phone: String,
    ) = if(phone == user.phoneNumber) {
        UserInfo(user.phoneNumber, user.name, user.bio)
    } else service.getUser(phone)
}