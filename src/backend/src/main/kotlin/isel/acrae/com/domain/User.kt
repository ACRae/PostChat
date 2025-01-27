package isel.acrae.com.domain
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet


data class User(
    val phoneNumber: String,
    val passwordValidator: String,
    val name : String
)

class UserMapper : RowMapper<User> {
    override fun map(rs: ResultSet, ctx: StatementContext): User {
        return User(
            rs.getString(1),
            rs.getString(2),
            rs.getString(3),
        )
    }
}


data class UserInfo(
    val phoneNumber: String,
    val name : String,
) {
    companion object {
        fun from(user: User) =
            UserInfo(
                user.phoneNumber,
                user.name
            )
    }
}

data class UserInfoList(
    val list: List<UserInfo>
)

class UserInfoMapper : RowMapper<UserInfo> {
    override fun map(rs: ResultSet, ctx: StatementContext): UserInfo {
        return UserInfo(
            rs.getString(1),
            rs.getString(2),
        )
    }
}