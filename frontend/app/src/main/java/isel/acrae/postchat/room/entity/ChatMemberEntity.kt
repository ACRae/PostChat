package isel.acrae.postchat.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "chat_member",
    primaryKeys = ["userId", "groupId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["phone_number"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChatMemberEntity(
    val userId: String,
    val groupId: Int
)