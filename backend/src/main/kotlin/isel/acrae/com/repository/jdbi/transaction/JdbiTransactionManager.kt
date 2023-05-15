package isel.acrae.com.repository.jdbi.transaction

import isel.acrae.com.repository.Transaction
import isel.acrae.com.repository.TransactionManager
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component


@Component
class JdbiTransactionManager(
    val productionDataSource: Jdbi,
) : TransactionManager {

    override fun <R> run(block: (Transaction) -> R): R =
        productionDataSource.inTransaction<R, Exception> { handle ->
            val transaction = JdbiTransaction(handle)
            block(transaction)
        }
}