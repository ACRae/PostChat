package isel.acrae.com.repository.jdbi.transaction

import isel.acrae.com.repository.RepositoryChat
import isel.acrae.com.repository.RepositoryTemplate
import isel.acrae.com.repository.RepositoryUser
import isel.acrae.com.repository.jdbi.RepositoryUserImpl
import isel.acrae.com.repository.Transaction
import isel.acrae.com.repository.jdbi.RepositoryChatImpl
import isel.acrae.com.repository.jdbi.RepositoryTemplateImpl
import org.jdbi.v3.core.Handle

class JdbiTransaction(private val handle: Handle) : Transaction {
    override val repositoryUser: RepositoryUser by lazy { RepositoryUserImpl(handle) }
    override val repositoryTemplate: RepositoryTemplate by lazy { RepositoryTemplateImpl(handle) }
    override val repositoryChat: RepositoryChat by lazy { RepositoryChatImpl(handle) }
}