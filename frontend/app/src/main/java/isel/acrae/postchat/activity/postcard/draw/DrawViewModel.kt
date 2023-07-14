package isel.acrae.postchat.activity.postcard.draw

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import isel.acrae.postchat.activity.postcard.draw.utils.PathProperties

class DrawViewModel : ViewModel() {
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
}