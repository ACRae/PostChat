package isel.acrae.postchat.service.mock


import isel.acrae.postchat.domain.TemplateList
import isel.acrae.postchat.service.TemplateDataService
import isel.acrae.postchat.service.mock.data.mockTemplate
import isel.acrae.postchat.service.mock.data.mockTokens

class TemplateDataMockService : TemplateDataService {
    override suspend fun getTemplates(token: String, templatesGotten: List<String>): TemplateList {
        mockTokens[token]!!
        return TemplateList(listOf(mockTemplate))
    }
}