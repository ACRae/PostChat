package isel.acrae.postchat.service


import isel.acrae.postchat.domain.TemplateList

interface TemplateDataService {
    suspend fun getTemplates(token: String, templatesGotten: List<String>): TemplateList
}