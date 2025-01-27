package isel.acrae.postchat.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey
    val phoneNumber: String,
    val name: String
) : RoomEntity