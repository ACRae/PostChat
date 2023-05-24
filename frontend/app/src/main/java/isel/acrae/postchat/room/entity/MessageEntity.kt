package isel.acrae.postchat.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "message",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatTo"],
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
    val templateName: String,
    val createdAt: String
) : RoomEntity