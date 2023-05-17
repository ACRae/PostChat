package isel.acrae.postchat.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.ui.composable.PostChatTopAppBar


@Composable
fun HomeScreen(
    getChatEntity: () -> List<ChatEntity>
) {
    val chats = getChatEntity.invoke()
    Scaffold(
        topBar = { PostChatTopAppBar() }
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
                .padding(top = 10.dp)
        ) {
            chats.forEach {
                item {
                    ChatItem(chatEntity = it)
                }
            }
        }
    }
}

@Composable
fun ChatItem(
    chatEntity: ChatEntity
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(20))
            .background(
                MaterialTheme
                    .colorScheme
                    .background
            )
            .padding(10.dp),
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

        Text(text = chatEntity.createdAt, fontSize = 12.sp)
    }
}

@Composable
@Preview
fun ChatItemPreview() {
    ChatItem(chatEntity = ChatEntity(
        1, "Test", "2023-12-2"
    ))
}


@Composable
@Preview
fun HomeScreenPreview() {
    fun getChatEntity() = listOf(
        ChatEntity(
            1, "Test", "2023-12-2"
        ),
        ChatEntity(
            1, "Test", "2023-12-2"
        )
    )
    HomeScreen(::getChatEntity)
}