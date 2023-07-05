package isel.acrae.com.repository

import isel.acrae.com.domain.Template


/**
 * Template Database operations
 */
interface RepositoryTemplate  {
    fun insertTemplate(template: Template)
    fun insertTemplates(templates: List<Template>)
    fun getTemplates(templatesGotten : List<String>) : List<Template>
    fun getTemplate(name : String) : Template?
}