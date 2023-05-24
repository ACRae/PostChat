package isel.acrae.postchat.activity.chat.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import isel.acrae.postchat.room.entity.UserEntity
import isel.acrae.postchat.ui.composable.PostChatTopAppBar
import isel.acrae.postchat.ui.theme.colorPallet

@Composable
fun ChatCreateScreen(
    getUsers: () -> Sequence<UserEntity>,
    createChat: () -> Unit,
) {
    val users = getUsers()
    var pickedUsers by remember { mutableStateOf(emptyList<String>()) }

    Scaffold(
        topBar = {
            PostChatTopAppBar(title = "Pick contacts for chat")
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.inverseSurface,
    ) { padding ->
        LazyColumn(
            Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            users.forEach {
                item {
                    UserItem(
                        phoneNumber = it.phoneNumber,
                        name = it.name,
                        onClick = {
                            pickedUsers = if(pickedUsers.contains(it))
                                pickedUsers.toMutableList().apply {
                                    remove(it)
                                }
                            else pickedUsers.toMutableList().apply {
                                add(it)
                            }
                        }
                    )
                }
            }
        }
        if(pickedUsers.isNotEmpty()) {
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
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                }
            }
        }
    }
}


@Composable
fun UserItem(
    phoneNumber: String, name: String,
    onClick: (String) -> Unit
) {
    var picked by remember { mutableStateOf(false) }
    val color by  remember { mutableStateOf(colorPallet.random()) }
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
            .clickable {
                onClick(name)
                picked = !picked
            },
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
                if(!picked)
                    Text(
                        text = name.first().toString(),
                        fontSize = 33.sp
                    )
                else Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null
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