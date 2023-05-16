package isel.acrae.postchat.service.web

import isel.acrae.postchat.domain.TemplateList
import isel.acrae.postchat.room.dao.TemplateDao
import isel.acrae.postchat.room.entity.TemplateEntity
import isel.acrae.postchat.service.TemplateDataService
import isel.acrae.postchat.service.web.mapper.EntityMapper
import isel.acrae.postchat.service.web.mapper.roomHandle
import okhttp3.OkHttpClient

class TemplateDataWebService(
    private val templateDao: TemplateDao,
    baseUrl : String,
    private val httpClient: OkHttpClient,
) : TemplateDataService, Web(baseUrl) {

    @Route("/template")
    override suspend fun getTemplates(token: String): List<TemplateEntity> =
        buildRequest(Get(makeURL(::getTemplates)), token)
            .send<TemplateList>(httpClient) { it.handle() }
            .roomHandle(templateDao) {
                insertAll(EntityMapper.from(it.list))
                getAll()
            }
}