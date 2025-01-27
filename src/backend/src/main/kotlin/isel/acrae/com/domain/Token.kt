package isel.acrae.com.domain

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.sql.Timestamp

data class Token(val content: String, val expires : Timestamp)

class TokenMapper : RowMapper<Token> {
    override fun map(rs: ResultSet, ctx: StatementContext): Token {
        return Token(
            rs.getString(1),
            rs.getTimestamp(2),
        )
    }
}
