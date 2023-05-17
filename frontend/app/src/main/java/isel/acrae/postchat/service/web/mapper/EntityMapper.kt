package isel.acrae.postchat.service.web.mapper


import isel.acrae.postchat.domain.Chat
import isel.acrae.postchat.domain.Domain
import isel.acrae.postchat.domain.Message
import isel.acrae.postchat.domain.Template
import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.room.dao.RoomDao
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.room.entity.TemplateEntity
import isel.acrae.postchat.room.entity.UserEntity

object EntityMapper {

    fun fromMessage(m: Message): MessageEntity =
        MessageEntity(
            m.id, m.userFrom, m.chatTo,
            m.mergedContent, m.handwrittenContent,
            m.templateName, m.createdMessageAt.toString()
        )

    fun fromChat(c: Chat): ChatEntity =
        ChatEntity(c.id, c.name, c.createdAt.toString())

    fun fromUserInfo(u: UserInfo): UserEntity =
        UserEntity(u.phoneNumber, u.name, u.bio)

    fun fromTemplate(t: Template): TemplateEntity =
        TemplateEntity(t.name, t.content)

    fun fromChatList(l : List<Chat>) =
        l.map { fromChat(it) }

    fun fromMessageList(l : List<Message>) =
        l.map { fromMessage(it) }

    fun fromTemplateList(l : List<Template>) =
        l.map { fromTemplate(it) }

    fun fromUserInfoList(l : List<UserInfo>) =
        l.map { fromUserInfo(it) }
}


inline fun <T : Domain, R : RoomDao, S> T.roomHandle(dao: R, block: R.(T) -> S): S {
    try {
        return block(dao, this)
    } catch (e: Exception) {
        throw e //to be handled
    }
}