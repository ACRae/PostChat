package isel.acrae.postchat.activity.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.acrae.postchat.domain.ChatHolder
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.ui.composable.PostChatTopAppBar
import isel.acrae.postchat.ui.theme.colorPallet


@Composable
fun HomeScreen(
    getChats: () -> Sequence<ChatHolder>,
    createChat: () -> Unit,
    onSettings: () -> Unit = {},
    onChat: (Int) -> Unit,
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
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.inverseSurface
    ) { padding ->
        LazyColumn(
            Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if(!chats.iterator().hasNext()) {
                item {
                    Text(
                        modifier = Modifier.offset(y = 20.dp),
                        text = "Nothing to show"
                    )
                }
            }
            else {
                chats.sortedByDescending { it.createdAt }.forEach {
                    item {
                        ChatItem(
                            chatHolder = it,
                            onClick = { onChat(it.id) }
                        )
                    }
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
                onClick = { createChat() }
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
    chatHolder: ChatHolder,
    onClick: () -> Unit = {},
) {
    val color by  remember {
        mutableStateOf(colorPallet.random())
    }

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
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chatHolder.name.first().toString(),
                    fontSize = 33.sp
                )
            }

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = chatHolder.name, fontSize = 21.sp
            )
        }

        Text(
            text = chatHolder.createdAt.take(16),
            fontSize = 12.sp
        )
    }
}