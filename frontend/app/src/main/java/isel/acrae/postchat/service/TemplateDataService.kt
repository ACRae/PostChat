package isel.acrae.postchat.service


import isel.acrae.postchat.domain.TemplateList
import isel.acrae.postchat.room.entity.TemplateEntity

interface TemplateDataService {
    suspend fun getTemplates(token: String): TemplateList
}