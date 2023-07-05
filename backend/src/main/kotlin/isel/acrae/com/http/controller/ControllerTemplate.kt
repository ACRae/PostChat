package isel.acrae.com.http.controller

import isel.acrae.com.domain.Template
import isel.acrae.com.domain.User
import isel.acrae.com.http.Routes
import isel.acrae.com.http.pipeline.Authenticate
import isel.acrae.com.service.ServiceTemplate
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Routes.Template.TEMPLATE)
class ControllerTemplate(
    private val service : ServiceTemplate
) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getTemplates(
        @Authenticate user: User,
        @RequestParam("gotten") templatesGotten: List<String>
    ) = service.getTemplates(templatesGotten)

    @Profile("test")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun insertTemplates(
        @Authenticate user: User,
        @RequestBody template: Template
    ) = service.insertTestTemplate(template)
}