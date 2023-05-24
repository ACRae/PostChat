package isel.acrae.postchat.service.web

import isel.acrae.postchat.domain.TemplateList
import isel.acrae.postchat.room.dao.TemplateDao
import isel.acrae.postchat.room.entity.TemplateEntity
import isel.acrae.postchat.service.TemplateDataService
import isel.acrae.postchat.service.web.mapper.EntityMapper
import isel.acrae.postchat.service.web.mapper.roomHandle
import okhttp3.OkHttpClient

class TemplateDataWebService(
    baseUrl : String,
    private val httpClient: OkHttpClient,
) : TemplateDataService, Web(baseUrl) {

    @Route("/template")
    override suspend fun getTemplates(token: String): TemplateList =
        buildRequest(Get(makeURL(::getTemplates)), token)
            .send(httpClient) { it.handle() }
}