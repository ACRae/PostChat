package isel.acrae.com.service

import isel.acrae.com.domain.Template
import isel.acrae.com.http.error.ApiIllegalArgumentException
import isel.acrae.com.http.error.ProblemTypeDetail
import isel.acrae.com.logger.logger
import isel.acrae.com.logger.runLogging
import isel.acrae.com.repository.TransactionManager
import isel.acrae.com.svg.SVG_EXTENSION
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.File
import kotlin.concurrent.thread

@Service
class ServiceTemplate(
    private val tManager : TransactionManager
) {
    companion object {
        private val logger = logger<ServiceTemplate>()
    }

    /**
     * Insert all templates located in [path] folder
     * @param [path] path to templates folder
     */
    @Profile("production")
    fun insertTemplates(path: String) {
        logger.runLogging(::insertTemplates) {
            val templatesFolder = File(path)
            tManager.run {
                logger.info("Templates Folder = $path")
                templatesFolder.listFiles()?.forEach { file ->
                    if(file.extension == SVG_EXTENSION) {
                        it.repositoryTemplate.insertTemplate(
                            Template(
                                file.nameWithoutExtension,
                                encodeBase64(file.readBytes())
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * Insert a template for test purposes
     */
    @Profile("test")
    fun insertTestTemplate(template: Template) {
        logger.runLogging(::insertTestTemplate) {
            tManager.run {
                if(template.name.isBlank())
                    throw ApiIllegalArgumentException(ProblemTypeDetail.INVALID_TEMPLATE_NAME)
                if(template.content.isBlank())
                    throw ApiIllegalArgumentException(ProblemTypeDetail.TEMPLATE_EMPTY_CONTENT)
                it.repositoryTemplate.insertTemplate(template)
            }
        }
    }


    /**
     * Gets all templates stored in the database
     */
    fun getTemplates(templatesGotten : List<String>?): TemplateList =
        logger.runLogging(::getTemplates) {
            tManager.run {
                TemplateList(
                    if(!templatesGotten.isNullOrEmpty()) {
                        it.repositoryTemplate.getTemplates(
                            templatesGotten
                        )
                    }
                    else it.repositoryTemplate.getTemplates()
                )
            }
        }
}