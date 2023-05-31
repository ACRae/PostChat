package isel.acrae.postchat.activity.postcard.draw

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Size
import isel.acrae.postchat.activity.postcard.draw.utils.PathProperties
import isel.acrae.postchat.domain.MessageInput
import isel.acrae.postchat.room.dao.MessageDao
import isel.acrae.postchat.service.Services
import isel.acrae.postchat.service.web.mapper.EntityMapper
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.Base64

class DrawViewModel(
    private val services: Services,
    private val messageDao: MessageDao,
    private val saveMessage: (ByteArray, String) -> Unit
) : ViewModel() {
    private var _pathPropList by mutableStateOf(emptyList<PathProperties>())
    val pathPropList : List<PathProperties>
        get() = _pathPropList

    private var _undoPathPopsList by mutableStateOf(emptyList<PathProperties>())
    private val undoPathPropsList : List<PathProperties>
        get() = _undoPathPopsList

    private var undoCounter by mutableStateOf(0)
    private val undoMax = 30

    fun addPathProperties(path: PathProperties) {
        _pathPropList = _pathPropList.toMutableList().apply {
            add(path)
        }
    }

    private fun removePathProperties(path: PathProperties) {
        _pathPropList = _pathPropList.toMutableList().apply {
            remove(path)
        }
    }

    fun undo() {
        if(pathPropList.isNotEmpty() && undoCounter < undoMax) {
            val lastItem = pathPropList.last()
            removePathProperties(lastItem)
            _undoPathPopsList = _undoPathPopsList.toMutableList().apply {
                add(lastItem)
            }
            undoCounter++
        }
    }

    fun redo() {
        if(undoPathPropsList.isNotEmpty()) {
            val lastPathUndone = undoPathPropsList.last()
            _undoPathPopsList = _undoPathPopsList.toMutableList().apply {
                remove(lastPathUndone)
            }
            addPathProperties(lastPathUndone)
            undoCounter--
        }
    }

    fun clear() {
        while (pathPropList.isNotEmpty()) {
            undo()
        }
    }

    fun resetUndo() {
        if(undoPathPropsList.isNotEmpty()) _undoPathPopsList = emptyList()
    }


    fun sendMessage(token: String, templateName: String, content: String, chatId: Int) {
        viewModelScope.launch {
            val res = try {
                Result.success(
                    services.chat.sendMessage(
                        token, MessageInput(content, templateName, Timestamp(System.currentTimeMillis())),
                        chatId
                    )
                )
            }catch (e : Exception) {
                Result.failure(e)
            }
            if(res.getOrNull() != null) {
                val value = res.getOrThrow()
                launch {
                    val bytes = Base64.getUrlDecoder().decode(
                        value.mergedContent
                    )
                    saveMessage(bytes, value.makeFileId())
                }

                messageDao.insert(EntityMapper.fromMessage(value))
            }
        }
    }

}