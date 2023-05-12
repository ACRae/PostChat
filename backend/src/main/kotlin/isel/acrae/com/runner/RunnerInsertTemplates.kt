package isel.acrae.com.runner

import isel.acrae.com.service.ServiceTemplate
import isel.acrae.com.workDir
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("production")
class RunnerInsertTemplates(
    private val service: ServiceTemplate
) : ApplicationRunner{

    /**
     * Runs this code every time the application starts.
     * Populates the database with templates.
     */
    override fun run(args: ApplicationArguments?) {
        service.insertTemplates("$workDir\\templates")
    }
}