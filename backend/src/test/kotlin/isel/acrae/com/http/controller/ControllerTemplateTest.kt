package isel.acrae.com.http.controller

import isel.acrae.com.*
import isel.acrae.com.http.Routes
import isel.acrae.com.service.TemplateList
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.http.HttpStatus

internal class ControllerTemplateTest : MockController() {

    @Test
    fun `get test templates`() {
        val token = registerUser()
        val templateRes = webTestClient.buildGet(
            Routes.Template.TEMPLATE, HttpStatus.OK, token
        )

        val templates = mapper.readValue(
            templateRes.responseBodyContent, TemplateList::class.java
        )
        assertEquals(templates.list.size, 1)
        assert(templates.list.map { it.name }.contains("test_template"))
    }

    @Test
    fun insertTemplates() {
    }
}