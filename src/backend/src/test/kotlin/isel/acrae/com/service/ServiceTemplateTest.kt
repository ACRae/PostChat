package isel.acrae.com.service

import isel.acrae.com.MockService
import isel.acrae.com.domain.Template
import isel.acrae.com.http.error.ApiIllegalArgumentException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ServiceTemplateTest : MockService() {

    @Test
    fun `insert valid template`() {
        runTest {
            serviceTemplate.insertTestTemplate(Template.TEST)
        }
    }

    @Test
    fun `insert invalid template`() {
        runTest {
            assertThrows<ApiIllegalArgumentException> {
                serviceTemplate.insertTestTemplate(Template.TEST.copy(""))
            }
        }
    }


    @Test
    fun `get zero templates`() {
        runTest {
            val template = serviceTemplate.getTemplates(null)
            assert(template.list.isEmpty())
        }
    }

    @Test
    fun `insert one template and get templates`() {
        runTest {
            serviceTemplate.insertTestTemplate(Template.TEST)
            val template = serviceTemplate.getTemplates(null)
            assert(template.list.size == 1)
        }
    }

    @Test
    fun `insert invalid template content`() {
        runTest {
            assertThrows<ApiIllegalArgumentException> {
                serviceTemplate.insertTestTemplate(Template.TEST.copy(content = ""))
            }
        }
    }

    @Test
    fun `get zero templates by tag`() {
        runTest {
            val template = serviceTemplate.getTemplates(listOf("non-existent-tag"))
            assert(template.list.isEmpty())
        }
    }

    @Test
    fun `insert one template and get templates by tag`() {
        runTest {
            serviceTemplate.insertTestTemplate(Template.TEST)
            val template = serviceTemplate.getTemplates(listOf("test-tag"))
            assert(template.list.size == 1)
        }
    }
}