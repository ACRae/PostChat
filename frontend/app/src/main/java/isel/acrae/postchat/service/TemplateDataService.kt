package isel.acrae.postchat.service

import isel.acrae.postchat.domain.TemplateList

interface TemplateDataService {

    @Route("/template")
    suspend fun getTemplates(token: String): TemplateList
}