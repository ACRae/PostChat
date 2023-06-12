package isel.acrae.postchat.activity.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import isel.acrae.postchat.room.AppDatabase
import kotlinx.coroutines.launch

class SettingsViewModel(private val db : AppDatabase) : ViewModel() {
    fun clearDb() {
        viewModelScope.launch {
            db.clearAllTables()
        }
    }
}