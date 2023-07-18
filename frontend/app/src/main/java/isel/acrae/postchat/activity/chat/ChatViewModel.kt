package isel.acrae.postchat.activity.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.room.dao.ChatDao
import isel.acrae.postchat.room.dao.MessageDao
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.web.mapper.EntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.sql.Timestamp
import java.util.Base64

class ChatViewModel(
    private val services: Services,
    private val messageDao: MessageDao,
    private val chatDao: ChatDao,
    private val saveMessage: (ByteArray, String) -> Unit
) : ViewModel() {
    private var _messages by mutableStateOf<List<MessageEntity>>(emptyList())

    val messages: List<MessageEntity>
        get() = _messages

    private var _chat by mutableStateOf<ChatEntity?>(null)
    val chat: ChatEntity?
        get() = _chat

    fun initialize(chatId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                while (true) {
                    getDbMessages(chatId)
                    Thread.sleep(1000)
                }
            }
        }
        getDbChat(chatId)
    }


    private fun getDbMessages(chatId: Int) {
        viewModelScope.launch {
            _messages = messageDao.getFromChat(chatId)
        }
    }

    private fun getDbChat(chatId: Int) {
        viewModelScope.launch {
            _chat = chatDao.get(chatId)
        }
    }

    fun sendMessage(
        token: String, templateName: String, path: String, chatId: Int,
        timestamp: Timestamp = Timestamp(System.currentTimeMillis())
    ): MutableLiveData<Boolean> {
        val done = MutableLiveData(false)
        viewModelScope.launch {
            val res = try {
                val file = File(path)
                val content = Base64.getUrlEncoder().encodeToString(file.readBytes())
                Result.success(
                    services.chat.sendMessage(
                        token, MessageInput(content, templateName, timestamp),
                        chatId
                    )
                ).also {
                    file.delete()
                }
            }catch (e : Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {
                val value = res.getOrThrow()
                val bytes = Base64.getUrlDecoder().decode(value.mergedContent)
                saveMessage(bytes, value.makeFileId())
                messageDao.insert(EntityMapper.fromMessage(value))
                done.value = true
            }
        }
        return done
    }
}