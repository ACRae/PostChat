package isel.acrae.postchat.activity.chat.info

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.acrae.postchat.domain.Chat
import isel.acrae.postchat.domain.UserInfo
import isel.acrae.postchat.ui.composable.PostChatTopAppBar
import isel.acrae.postchat.ui.theme.colorPallet


@Composable
fun ChatInfoScreen(
    getChatProps: () -> Chat,
    getUserInfo: () -> Sequence<UserInfo>
) {
    val chat = getChatProps()
    val users = getUserInfo()
    Scaffold(
        topBar = { PostChatTopAppBar( "Info - " + chat.name ) }
    ) { padding ->

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            users.forEach {
                item {
                    UserItem(phoneNumber = it.phoneNumber, name = it.name)
                }
            }
        }
   }
}


@Composable
fun UserItem(
    phoneNumber: String, name: String,
) {
    val color by remember { mutableStateOf(colorPallet.random()) }
    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
            .clip(RoundedCornerShape(20))
            .background(
                MaterialTheme
                    .colorScheme
                    .primaryContainer
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
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.first().toString(),
                    fontSize = 33.sp
                )
            }

            Text(
                modifier = Modifier.padding(start = 20.dp),
                text = phoneNumber, fontSize = 21.sp
            )
        }

        Text(
            text = name,
            fontSize = 12.sp
        )
    }
}


