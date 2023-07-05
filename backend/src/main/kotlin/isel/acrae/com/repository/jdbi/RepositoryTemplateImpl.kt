package isel.acrae.com.repository.jdbi

import isel.acrae.com.domain.Template
import isel.acrae.com.domain.TemplateMapper
import isel.acrae.com.repository.RepositoryTemplate
import org.jdbi.v3.core.Handle

class RepositoryTemplateImpl(private val handle: Handle) : RepositoryTemplate {

    override fun insertTemplate(template: Template) {
        handle.createUpdate(
            """
        insert into template (name, content) values (:name, :content)
        on conflict do nothing
        """
        )
            .bind("name", template.name)
            .bind("content", template.content)
            .execute()
    }

    override fun insertTemplates(templates: List<Template>) {
        templates.forEach {
            insertTemplate(it)
        }
    }

    override fun getTemplates(templatesGotten: List<String>): List<Template> =
        handle.createQuery(
            """
            select name, content from template 
            where name
            not in (${templatesGotten.joinToString { "'$it'" }})
            """.trimIndent()
        )
            .map(TemplateMapper())
            .list()

    override fun getTemplate(name: String): Template? =
        handle.createQuery(
            """
            select name, content from template where name = :name
            """.trimIndent()
        )
            .bind("name", name)
            .map(TemplateMapper())
            .firstOrNull()
}