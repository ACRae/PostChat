package isel.acrae.postchat.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import isel.acrae.postchat.room.entity.ChatMemberEntity

@Dao
interface ChatMemberDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chatMemberEntity: ChatMemberEntity)
}
