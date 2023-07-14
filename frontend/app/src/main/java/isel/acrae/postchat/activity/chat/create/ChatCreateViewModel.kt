package isel.acrae.postchat.activity.chat.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.CreateChatInput
import isel.acrae.postchat.room.dao.ChatDao
import isel.acrae.postchat.room.dao.UserDao
import isel.acrae.postchat.room.entity.UserEntity
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.web.mapper.EntityMapper
import kotlinx.coroutines.launch
import java.sql.Timestamp

class ChatCreateViewModel(
    private val services: Services,
    private val userDao: UserDao,
    private val chatDao: ChatDao,
) : ViewModel() {
    private var _users by mutableStateOf<Sequence<UserEntity>>(emptySequence())


    val users: Sequence<UserEntity>
        get() {
            getDbUsers()
            return _users
        }


    private fun getDbUsers() {
        viewModelScope.launch {
            _users = userDao.getAll().asSequence()
        }
    }

    fun createChat(chatName: String, phoneNumbers: List<String>, token: String): MutableLiveData<Int?> {
        val chatIdLiveData = MutableLiveData<Int?>(null)
        viewModelScope.launch {
            val res = try {
                Result.success(
                    services.chat.createChat(token, CreateChatInput(
                        phoneNumbers, chatName, Timestamp(System.currentTimeMillis()))
                    )
                )
            }
            catch (e : Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {
                chatDao.insert(
                    EntityMapper.fromChat(res.getOrThrow())
                )
                chatIdLiveData.value = res.getOrThrow().id
            }
        }
        return chatIdLiveData
    }
}