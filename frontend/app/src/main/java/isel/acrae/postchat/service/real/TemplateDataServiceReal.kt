package isel.acrae.postchat.service.real

import isel.acrae.postchat.domain.TemplateList
import isel.acrae.postchat.service.TemplateDataService

class TemplateDataServiceReal : TemplateDataService {
    override suspend fun getTemplates(token: String): TemplateList {
        TODO("Not yet implemented")
    }
}