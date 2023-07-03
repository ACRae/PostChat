package isel.acrae.com.domain

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.sql.Timestamp


data class MessageHolder(
    val id : Int,
    val userFrom : String,
    val chatTo : Int,
    val chatName: String?,
    val content : String,
    val templateName: String,
    val backplate: String,
    val createdChatAt : Timestamp,
    val createdMessageAt : Timestamp
)


class MessageMapper : RowMapper<MessageHolder> {
    override fun map(rs: ResultSet, ctx: StatementContext): MessageHolder {
        return MessageHolder(
            rs.getInt(1),
            rs.getString(2),
            rs.getInt(3),
            rs.getString(4),
            rs.getString(5),
            rs.getString(6),
            rs.getString(7),
            rs.getTimestamp(8),
            rs.getTimestamp(9)
        )
    }
}


data class Chat (
    val id : Int,
    val name: String,
    val createdAt: Timestamp,
    val lastMessage: Timestamp? = null,
)


data class ChatInfo(
    val props: Chat,
    val usersInfo: List<UserInfo>
)


class ChatMapper : RowMapper<Chat> {
    override fun map(rs: ResultSet, ctx: StatementContext): Chat {
        return Chat(
            rs.getInt(1),
            rs.getString(2),
            rs.getTimestamp(3),
            try {
                rs.getTimestamp(4)
            }catch (e : Exception) {
                null
            }
        )
    }
}