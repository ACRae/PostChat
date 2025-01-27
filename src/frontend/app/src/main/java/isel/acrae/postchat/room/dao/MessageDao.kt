package isel.acrae.postchat.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import isel.acrae.postchat.room.entity.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(messageEntity: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<MessageEntity>)

    @Query("SELECT * FROM MESSAGE")
    suspend fun getAll() : List<MessageEntity>

    @Query("SELECT t1.*\n" +
            "FROM MESSAGE t1\n" +
            "JOIN (\n" +
            "  SELECT chatTo, MAX(createdAt) AS latestCreatedAt\n" +
            "  FROM MESSAGE\n" +
            "  GROUP BY chatTo\n" +
            ") t2 ON t1.chatTo = t2.chatTo AND t1.createdAt = t2.latestCreatedAt")
    suspend fun getLatestDistinct() : List<MessageEntity>

    @Query("SELECT * FROM MESSAGE WHERE id = :id")
    suspend fun get(id : Int) : MessageEntity

    @Query("SELECT * FROM MESSAGE WHERE chatTo = :chatId ORDER BY createdAt DESC")
    suspend fun getFromChat(chatId : Int) : List<MessageEntity>

    @Query("SELECT * FROM MESSAGE LIMIT :limit OFFSET :offset")
    suspend fun getAllPaginated(limit : Int, offset: Int) : List<MessageEntity>
}
