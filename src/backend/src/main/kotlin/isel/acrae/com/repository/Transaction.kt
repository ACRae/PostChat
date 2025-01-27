package isel.acrae.com.repository


interface Transaction {
    val repositoryUser : RepositoryUser
    val repositoryTemplate : RepositoryTemplate
    val repositoryChat : RepositoryChat
}

interface TransactionManager{
    fun <R> run(block: (Transaction) -> R) : R
}


