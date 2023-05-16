package isel.acrae.postchat.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(
    tableName = "message",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["phoneNumber"],
            childColumns = ["userFrom"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["chatTo"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MessageEntity(
    @PrimaryKey
    val id: Int,
    val userFrom: String,
    val chatTo: Int,
    val mergedContent: String,
    val handwrittenContent: String,
    val templateName: String?,
    val createdAt: Timestamp
) : RoomEntity