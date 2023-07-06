package isel.acrae.postchat.activity.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.ui.composable.PopDialog
import isel.acrae.postchat.ui.composable.PostChatTopAppBar

@Composable
fun SettingsScreen(
    onInfo: () -> Unit = {},
    onLogout: () -> Unit = {},
    onClearLocalDb: () -> Unit = {},
    onListMessagesDb: () -> String?,
    onListChatDb: () -> String?,
    onListWebChats: () -> String?,
    onDeleteUser: () -> Unit,
) {
    var pop  by remember { mutableStateOf(false) }
    var popContent by remember { mutableStateOf("") }
    var delete by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            PostChatTopAppBar {
                IconButton(
                    modifier = Modifier.offset((-10).dp),
                    onClick =  { onInfo() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        },
        contentColor = MaterialTheme.colorScheme.inverseSurface
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SettingEntry(title = "Logout", onLogout)
            SettingEntry(title = "Delete User") {
                popContent = "Are you sure?"
                pop = true
                delete = true
            }
            SettingEntry(title = "Clear local db", onClearLocalDb)
            SettingEntry(title = "List All Db Messages") {
                popContent = onListMessagesDb() ?: ""
                pop = true
            }
            SettingEntry(title = "List All Db Chats") {
                popContent = onListChatDb() ?: ""
                pop = true
            }
            SettingEntry(title = "List All Web Chats") {
                popContent = onListWebChats() ?: ""
                pop = true
            }
            if(pop) {
                PopDialog(content = {
                    Text(text = popContent)
                }, onDismiss = { pop = false},
                    onConfirm = if(delete)
                        onDeleteUser else null
                )
            }
        }
    }
}


@Composable
fun SettingEntry(
    title: String,
    onClick : () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(3.dp)
            .clip(RoundedCornerShape(10))
            .clickable { onClick() }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .padding(end = 5.dp)
        )
        Text(
            text = title,
            fontSize = 23.sp,
        )
    }
}
