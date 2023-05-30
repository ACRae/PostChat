package isel.acrae.postchat.activity.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.room.dao.ChatDao
import isel.acrae.postchat.room.dao.MessageDao
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.service.Services
import kotlinx.coroutines.launch

class ChatViewModel(
    private val services: Services,
    private val messageDao: MessageDao,
    private val chatDao: ChatDao,
) : ViewModel() {
    private var _messages by mutableStateOf<Sequence<MessageEntity>>(emptySequence())

    val messages: Sequence<MessageEntity>
        get() {
            return _messages
        }

    private var _chat by mutableStateOf<ChatEntity?>(null)
    val chat: ChatEntity?
        get() = _chat


    fun initialize(chatId: Int) {
        getDbMessages(chatId)
        getDbChat(chatId)
    }


    private fun getDbMessages(chatId: Int) {
        viewModelScope.launch {
            _messages = messageDao.getFromChat(chatId).asSequence()
        }
    }

    private fun getDbChat(chatId: Int) {
        viewModelScope.launch {
            _chat = chatDao.get(chatId)
        }
    }

}