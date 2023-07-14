package isel.acrae.postchat.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class MessageEntity(
    @PrimaryKey
    val id: Int,
    val userFrom: String,
    val chatTo: Int,
    val mergedContent: String,
    val handwrittenContent: String,
    val templateName: String,
    val createdAt: String,
    val fileName: String
) : RoomEntity