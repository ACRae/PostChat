package isel.acrae.postchat.activity.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.ChatHolder
import isel.acrae.postchat.room.dao.ChatDao
import isel.acrae.postchat.room.dao.MessageDao
import isel.acrae.postchat.room.dao.TemplateDao
import isel.acrae.postchat.room.dao.UserDao
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.web.mapper.EntityMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Base64

class HomeViewModel(
    private val services: Services,
    private val userDao: UserDao,
    private val chatDao: ChatDao,
    private val messageDao: MessageDao,
    private val templateDao: TemplateDao,
    private val saveTemplate: (ByteArray, String) -> Unit,
    private val saveMessage: (ByteArray, String) -> Unit
) : ViewModel() {
    private var _chats by mutableStateOf<Sequence<ChatEntity>>(emptySequence())
    val chats: Sequence<ChatHolder>
        get() {
            getDbChats()
            getDbMessages()
            val latestMess = latestMessages
            val latestMessId = latestMess.map { m -> m.chatTo }
            val chatsTemp = _chats
            return chatsTemp.map {
                if(latestMessId.contains(it.id))
                    ChatHolder.from(
                        it.copy(createdAt = latestMessages.first {
                                m -> m.chatTo == it.id }.createdAt), true
                    )
                else ChatHolder.from(it, false)
            }
        }

    private var _messages by mutableStateOf<Sequence<MessageEntity>>(emptySequence())
    val latestMessages: Sequence<MessageEntity>
        get() {
            getDbMessages()
            return _messages
        }

    fun initialize(token: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                while (true) {
                    Log.i("POLLING", "HOME ACTIVITY IS POLLING")
                    getWebChats(token)
                    getWebMessages(token)
                    getWebTemplates(token)
                    Thread.sleep(10000)
                }
            }
        }
    }

    private fun getWebChats(token : String) {
        viewModelScope.launch {
            val res = try {
                Result.success(services.chat.getChats(token))
            }catch (e : Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {
                chatDao.insertAll(
                    EntityMapper.fromChatList(res.getOrThrow().list)
                )
            }
        }
    }

    private fun getWebTemplates(token: String) {
        viewModelScope.launch {
            val templates = templateDao.getAll().map { it.name }
            val res = try {
                Result.success(
                    services.template.getTemplates(token, templates)
                )
            }catch (e: Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {

                val list = res.getOrThrow().list

                Log.i("Return", list.toString())

                list.forEach {
                    val bytes = Base64.getUrlDecoder().decode(
                        it.content
                    )
                    saveTemplate(bytes, it.name)
                }

                templateDao.insertAll(
                    EntityMapper.fromTemplateList(list)
                )
            }
        }
    }


    fun getWebUsers(
        token : String, users: List<String>
    ) {
        viewModelScope.launch {
            val res = try {
                Result.success(
                    services.user.getUsers(
                    token, users
                ))
            }catch (e : Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {
                userDao.insertAll(
                    EntityMapper.fromUserInfoList(res.getOrThrow().list)
                )
            }
        }
    }

    private fun getWebMessages(token: String) {
        viewModelScope.launch {
            val res = try {
                Result.success(services.chat.getMessages(token))
            }catch (e : Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {
                val list = res.getOrThrow().list

                list.forEach {
                    launch {
                        val bytes = Base64.getUrlDecoder().decode(
                            it.mergedContent
                        )
                        saveMessage(bytes, it.makeFileId())
                    }
                }
                messageDao.insertAll(
                    EntityMapper.fromMessageList(list)
                )
            }
        }
    }

    private fun getDbChats() {
        viewModelScope.launch {
            _chats = chatDao.getAll().asSequence()
        }
    }

    private fun getDbMessages() {
        viewModelScope.launch {
            _messages = messageDao.getLatestDistinct().asSequence()
        }
    }

}