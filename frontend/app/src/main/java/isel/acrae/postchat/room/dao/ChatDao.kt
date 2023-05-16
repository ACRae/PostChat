package isel.acrae.postchat.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import isel.acrae.postchat.room.entity.ChatEntity

@Dao
interface ChatDao : RoomDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chatEntity: ChatEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<ChatEntity>)

    @Query("SELECT * FROM CHAT")
    suspend fun getAll() : List<ChatEntity>

    @Query("SELECT * FROM CHAT LIMIT :limit OFFSET :offset")
    suspend fun getAllPaginated(limit : Int, offset: Int) : List<ChatEntity>

}
