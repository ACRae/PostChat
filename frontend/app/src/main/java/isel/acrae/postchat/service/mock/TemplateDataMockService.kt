package isel.acrae.postchat.service.mock


import isel.acrae.postchat.room.entity.TemplateEntity
import isel.acrae.postchat.service.TemplateDataService
import isel.acrae.postchat.service.mock.data.mockTemplate
import isel.acrae.postchat.service.mock.data.mockTokens

class TemplateDataMockService : TemplateDataService {
    override suspend fun getTemplates(token: String): List<TemplateEntity> {
        mockTokens[token]!!
        return listOf(mockTemplate)
    }
}