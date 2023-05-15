package isel.acrae.postchat.service.mock

import isel.acrae.postchat.domain.TemplateList
import isel.acrae.postchat.service.TemplateDataService

class TemplateDataMockService : TemplateDataService {
    override suspend fun getTemplates(token: String): TemplateList {
        TODO("Not yet implemented")
    }
}