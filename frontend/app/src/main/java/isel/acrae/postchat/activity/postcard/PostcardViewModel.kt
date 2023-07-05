package isel.acrae.postchat.activity.postcard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.domain.HandwrittenInput
import isel.acrae.postchat.room.dao.MessageDao
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.service.Services
import kotlinx.coroutines.launch

class PostcardViewModel(
    private val services: Services,
    private val messageDao: MessageDao,
) : ViewModel() {
    private var _message by mutableStateOf<MessageEntity?>(null)

    val message : MessageEntity?
        get() = _message


    fun htr(token: String, handwrittenInput: HandwrittenInput) : MutableLiveData<String?> {
        val text = MutableLiveData<String?>(null)
        viewModelScope.launch {
            text.value = try {
                services.chat.htrMessage(token, handwrittenInput)
            } catch (e : Exception) {
                "Error"
            }
        }
        return text
    }

    fun getDbMessage(id: Int) {
        viewModelScope.launch {
            _message = messageDao.get(id)
        }
    }

}