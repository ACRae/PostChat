package isel.acrae.postchat.service.web

import isel.acrae.postchat.domain.TemplateList
import isel.acrae.postchat.service.TemplateDataService
import okhttp3.OkHttpClient

class TemplateDataWebService(
    baseUrl : String,
    private val httpClient: OkHttpClient,
) : TemplateDataService, Web(baseUrl) {

    @Route("/template")
    override suspend fun getTemplates(token: String, templatesGotten: List<String>): TemplateList =
        buildRequest(Get(makeURL(::getTemplates)
            .addQuery(QueryParam.from("gotten", templatesGotten))), token)
            .send(httpClient) { it.handle() }
}