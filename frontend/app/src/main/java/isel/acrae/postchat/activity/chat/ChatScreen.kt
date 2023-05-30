package isel.acrae.postchat.activity.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.ui.composable.PostChatTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    getMessages: () -> Sequence<MessageEntity>,
    chat: ChatEntity,
    templates: List<ChatActivity.TemplateHolder>,
    onEdit: (String) -> Unit
) {
    val scrollState = rememberLazyListState()
    val context = LocalContext.current
    var chosenTemplate by remember { mutableStateOf("") }

    BottomSheetScaffold(
        topBar = {
            PostChatTopAppBar(title = chat.name)
        },
        sheetContent = {
            Column(
                Modifier
                    .offset(y = (-25).dp)
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if(chosenTemplate.isBlank()) {
                    BasicText(
                        modifier = Modifier.padding(
                            top = 20.dp, bottom = 20.dp
                        ),
                        text = "Choose a postcard to edit"
                    )
                } else{
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 15.dp, start = 15.dp),
                        onClick = { onEdit(chosenTemplate) },
                    ) {
                        Text("Edit")
                    }
                }
                templates.forEach {
                    Column {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(it.path)
                                .decoderFactory(SvgDecoder.Factory())
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(20.dp)
                                .clickable {
                                    chosenTemplate = if (chosenTemplate != it.name) it.name else ""
                                },
                            colorFilter = if(chosenTemplate == it.name)
                                ColorFilter.tint(Color.LightGray, BlendMode.Darken)
                            else null
                        )
                    }
                }
            }
        },
        sheetPeekHeight = 90.dp
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Messages(
                getMessages = getMessages,
                navigateToProfile = {},
                scrollState = scrollState
            )
        }
    }
}


@Composable
fun Messages(
    getMessages: () -> Sequence<MessageEntity>,
    navigateToProfile: (String) -> Unit,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val messages = getMessages()
    Box(modifier = modifier) {

        val authorMe = "Me"
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            messages.forEach {

                item {
                    DayHeader(it.createdAt.replaceAfter(".", " "))
                }

                item {
                    Message(
                        onAuthorClick = { name -> navigateToProfile(name) },
                        msg = it,
                        user = it.userFrom,
                    )
                }
            }
        }
    }
}

@Composable
fun Message(
    onAuthorClick: (String) -> Unit,
    msg: MessageEntity,
    user: String,
) {
    val borderColor = MaterialTheme.colorScheme.primary

    val spaceBetweenAuthors = Modifier.padding(top = 8.dp)
    Row(modifier = spaceBetweenAuthors) {
        Spacer(modifier = Modifier.width(74.dp))
        AuthorAndTextMessage(
            msg = msg,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}

@Composable
fun AuthorAndTextMessage(
    msg: MessageEntity,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        //if (isLastMessageByAuthor) {
        //    AuthorNameTimestamp(msg)
        //}
        ChatItemBubble(msg, isUserMe = false)
        // Between bubbles
        Spacer(modifier = Modifier.height(4.dp))
    }
}

private val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

@Composable
fun ChatItemBubble(
    message: MessageEntity,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit = { _ -> }
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Column {
        Surface(
            color = backgroundBubbleColor,
            shape = ChatBubbleShape
        ) {
            ClickableMessage(message = message)
        }
    }
}

@Composable
fun ClickableMessage(
    message: MessageEntity,
) {
    Text(
        text = message.mergedContent,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp)
    )
}


@Composable
private fun AuthorNameTimestamp(msg: MessageEntity) {
    // Combine author and timestamp for a11y.
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = msg.userFrom,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = msg.createdAt,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

private val JumpToBottomThreshold = 56.dp