package isel.acrae.com

import isel.acrae.com.http.pipeline.interceptor.InterceptorAuthentication
import isel.acrae.com.http.pipeline.interceptor.InterceptorContentType
import isel.acrae.com.http.pipeline.interceptor.InterceptorLogger
import isel.acrae.com.http.pipeline.resolver.UserIdArgumentResolver
import org.jdbi.v3.core.Jdbi
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import javax.sql.DataSource


val templatesDir: String = System.getenv("POSTCHAT_TEMPLATES")
val database : String = System.getenv("POSTCHAT_DB_CONNECTION")
val testDatabase : String = System.getenv("POSTCHAT_DB_TEST_CONNECTION")
val pythonSourceDir: String = System.getenv("POSTCHAT_HTR")


@Configuration
class PipelineConfigurer(
    val contentTypeInterceptor: InterceptorContentType,
    val loggerInterceptor: InterceptorLogger,
    val authenticationInterceptor: InterceptorAuthentication,
    val userIdArgumentResolver: UserIdArgumentResolver,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(contentTypeInterceptor)
        registry.addInterceptor(loggerInterceptor)
        registry.addInterceptor(authenticationInterceptor)
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userIdArgumentResolver)
    }
}

@SpringBootApplication
class PostChatApplication {

    @Bean
    fun jdbi(dataSource: DataSource): Jdbi = Jdbi.create(dataSource)

    @Bean
    @Profile("production")
    fun pgDataSource(): DataSource =
        PGSimpleDataSource().apply { setURL(database) }

    @Bean
    @Profile("test")
    fun pgMockDataSource(): DataSource =
        PGSimpleDataSource().apply { setURL(testDatabase) }
}

fun main() {
    runApplication<PostChatApplication>()
}
