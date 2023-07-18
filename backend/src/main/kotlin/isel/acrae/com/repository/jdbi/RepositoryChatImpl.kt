package isel.acrae.com.repository.jdbi

import isel.acrae.com.domain.*
import isel.acrae.com.repository.RepositoryChat
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import java.sql.Timestamp

class RepositoryChatImpl(private val handle : Handle) : RepositoryChat{
    override fun getMessages(phoneNumber: String): List<MessageHolder> =
        handle.createQuery(
            """
            update message as m
            set obtained = true
            from (
                     select m.id, m.obtained, m.chat_to, cg.name, m.template_name
                     from chat_group_member as cgm
                              join _user as ui on ui.phone_number = cgm.user_id
                              join chat_group as cg on cgm.group_id = cg.id
                              join message as m on cg.id = m.chat_to
                     where cgm.user_id = :phoneNumber and m.obtained = false and m.user_from != :phoneNumber
                 ) as subquery
                 JOIN chat_group AS cg ON subquery.chat_to = cg.id
                 join template t on subquery.template_name = t.name
            where subquery.id = m.id
            returning m.id, m.user_from, m.chat_to, cg.name, 
            m.content, m.template_name, t.content, cg.created_at, m.created_at;
            """.trimIndent()
        )
            .bind("phoneNumber", phoneNumber)
            .map(MessageMapper())
            .list()

    override fun sendMessage(
        userFromPhone: String,
        content: String,
        templateName: String,
        chatId: Int,
        timestamp: Timestamp
    ): Int? =
        handle.createUpdate(
            """    
            insert into message(user_from, chat_to, content, template_name, created_at) 
            values(:userFrom, :chatTo, :content, :templateName, :timestamp) 
            """.trimIndent()
        )
            .bind("userFrom", userFromPhone)
            .bind("chatTo", chatId)
            .bind("content", content)
            .bind("templateName", templateName)
            .bind("timestamp", timestamp)
            .executeAndReturnGeneratedKeys("id")
            .mapTo<Int>()
            .first()

    override fun getChat(chatId: Int, phoneNumber: String): Chat? =
        handle.createQuery(
            """
                select cg.id, cg.name, cg.created_at from chat_group cg
                join chat_group_member cgm on cgm.group_id = cg.id
                join _user u on cgm.user_id = u.phone_number 
                where u.phone_number = :userId and cg.id = :chatId
            """.trimIndent()
        )
            .bind("userId", phoneNumber)
            .bind("chatId", chatId)
            .map(ChatMapper())
            .firstOrNull()

    override fun getUserChats(phoneNumber: String): List<Chat> =
        handle.createQuery(
            """
                SELECT chat_group.id, chat_group.name, chat_group.created_at
                FROM chat_group
                JOIN chat_group_member ON chat_group.id = chat_group_member.group_id
                WHERE chat_group_member.user_id = :userId
                GROUP BY chat_group.id
            """.trimIndent()
        )
            .bind("userId", phoneNumber)
            .map(ChatMapper())
            .list()

    override fun getChatMembers(chatId: Int, phoneNumber: String): List<UserInfo> =
        handle.createQuery(
            """
                select u.phone_number, u.name from chat_group cg
                join chat_group_member cgm on cgm.group_id = cg.id
                join _user u on cgm.user_id = u.phone_number 
                where cg.id = :chatId
            """.trimIndent()
        )
            .bind("chatId", chatId)
            .map(UserInfoMapper())
            .list()

    override fun getPrivateChat(phoneNumber1: String, phoneNumber2: String): Chat? =
        handle.createQuery(
            """
            SELECT cg.id, cg.name, cg.created_at 
            FROM chat_group cg
            JOIN chat_group_member cgm1 ON cg.id = cgm1.group_id AND cgm1.user_id = :user1Id
            JOIN chat_group_member cgm2 ON cg.id = cgm2.group_id AND cgm2.user_id = :user2Id
            GROUP BY cg.id, cg.name, cg.created_at
            HAVING COUNT(*) = 1
            """.trimIndent()
        )
            .bind("user1Id", phoneNumber1)
            .bind("user2Id", phoneNumber2)
            .map(ChatMapper())
            .firstOrNull()


    override fun createChat(name: String?, timestamp: Timestamp): Int? =
        handle.createUpdate(
            """
                insert into chat_group(name, created_at) 
                values(:name, :created_at)
                 
            """.trimIndent()
        )
            .bind("name", name)
            .bind("created_at", timestamp)
            .executeAndReturnGeneratedKeys("id")
            .mapTo<Int>()
            .first()

    override fun insertChatMember(phoneNumber: String, chatId: Int) {
        handle.createUpdate(
            """
                insert into chat_group_member(user_id, group_id) 
                values (:userId, :chatId)
            """.trimIndent()
        )
            .bind("userId", phoneNumber)
            .bind("chatId", chatId)
            .execute()
    }
}