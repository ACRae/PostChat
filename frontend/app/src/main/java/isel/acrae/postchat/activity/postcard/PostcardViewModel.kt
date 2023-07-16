package isel.acrae.postchat.activity.postcard

import android.util.Log
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

    private var _htrMessage by mutableStateOf<Result<String>?>(null)

    val htrMessage : Result<String>?
        get() = _htrMessage

    fun htr(token: String, handwrittenInput: HandwrittenInput) : MutableLiveData<Boolean> {
        val done = MutableLiveData(false)
        Log.i("PERFORMING HTR", "")
        val msg = _htrMessage
        if(msg == null || msg.isFailure) {
            viewModelScope.launch {
                _htrMessage = try {
                    Result.success(
                        services.chat.htrMessage(token, handwrittenInput).text
                    )
                } catch (e: Exception) {
                    Result.failure(e)
                }
                done.value = true
            }
        } else done.value = true
        return done
    }

    fun getDbMessage(id: Int) {
        viewModelScope.launch {
            _message = messageDao.get(id)
        }
    }
}