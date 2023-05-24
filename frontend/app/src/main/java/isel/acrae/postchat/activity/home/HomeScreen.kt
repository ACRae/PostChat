package isel.acrae.postchat.activity.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.ui.composable.PostChatTopAppBar


@Composable
fun HomeScreen(
    getChats: () -> Sequence<ChatEntity>,
    getMessages: () -> Sequence<MessageEntity>,
    createChat: (String, List<String>) -> Unit,
    onSettings: () -> Unit = {},
) {
    val chats = getChats()

    Scaffold(
        topBar = {
            PostChatTopAppBar {
                IconButton(
                    modifier = Modifier.offset((-10).dp),
                    onClick = onSettings
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    MaterialTheme
                        .colorScheme
                        .primaryContainer
                )
        ) {
            chats.forEach {
                item {
                    ChatItem(chatEntity = it)
                }
            }
        }

        Row(
            Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(top = 5.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Default.Message,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun ChatItem(
    chatEntity: ChatEntity,
    onClick: () -> Unit = {},
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(20))
            .background(
                MaterialTheme
                    .colorScheme
                    .background
            )
            .padding(10.dp)
            .clickable { onClick.invoke() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .padding(5.dp)
                    .size(60.dp)
                    .clip(
                        RoundedCornerShape(100)
                    )
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chatEntity.name.first().toString(),
                    fontSize = 33.sp
                )
            }

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = chatEntity.name, fontSize = 21.sp
            )
        }

        Text(
            text = chatEntity.createdAt.toString()
                .replaceAfter(".", " "),
            fontSize = 12.sp
        )
    }
}

@Composable
@Preview
fun ChatItemPreview() {
    ChatItem(
        chatEntity = ChatEntity(
            1, "Test", "2023-05-11 21:15:02.602668"
        )
    )
}


@Composable
@Preview
fun HomeScreenPreview() {
    fun getChatEntity() = sequenceOf(
        ChatEntity(
            1, "Test1", "2023-05-11 21:15:02.602668"
        ),
        ChatEntity(
            2, "Test2", "2023-05-11 21:15:02.620371"
        )
    )
    HomeScreen(::getChatEntity, { sequenceOf() }, { _, _ -> }, {})
}