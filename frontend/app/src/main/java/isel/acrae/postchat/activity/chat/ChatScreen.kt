package isel.acrae.postchat.activity.chat

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import isel.acrae.postchat.room.entity.ChatEntity
import isel.acrae.postchat.room.entity.MessageEntity
import isel.acrae.postchat.ui.composable.JumpToBottom
import isel.acrae.postchat.ui.composable.PostChatTopAppBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    me: String,
    messageDir: String,
    getMessages: () -> List<MessageEntity>,
    currTempMessagePath: String?,
    templateName: String?,
    chat: ChatEntity,
    templates: List<ChatActivity.TemplateHolder>,
    onEdit: (String) -> Unit,
    onPostcardClick: (String) -> Unit,
    onSendMessage: (template: String, path: String) -> Unit,
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
                    .fillMaxWidth()
                    .offset(y = (-25).dp)
                    .padding(start = 10.dp, end = 10.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if(!currTempMessagePath.isNullOrBlank() && templateName != null) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 15.dp, start = 15.dp),
                        onClick = { onSendMessage(templateName, currTempMessagePath) },
                    ) {
                        Text("Send")
                    }
                    Column {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(currTempMessagePath)
                                .decoderFactory(SvgDecoder.Factory())
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.clickable { onPostcardClick(currTempMessagePath) }
                        )
                    }
                }else {
                    if (chosenTemplate.isBlank()) {
                        BasicText(
                            modifier = Modifier.padding(
                                top = 20.dp, bottom = 20.dp
                            ),
                            text = "Choose a postcard to edit"
                        )
                    } else {
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
                                colorFilter = if (chosenTemplate == it.name)
                                    ColorFilter.tint(Color.LightGray, BlendMode.Darken)
                                else null
                            )
                        }
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
                me,
                messageDir,
                getMessages = getMessages,
                scrollState = scrollState,
                onPostcardClick = onPostcardClick,
            )
        }
    }
}


@Composable
fun Messages(
    me: String,
    messageDir: String,
    getMessages: () -> List<MessageEntity>,
    scrollState: LazyListState,
    modifier: Modifier = Modifier,
    onPostcardClick: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val messages = getMessages()
    Box(modifier = modifier) {
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            var prevAuthor: String? = null
            messages.forEach {
                val isFirstMessageByAuthor = prevAuthor != null && prevAuthor != it.userFrom

                item {
                    DayHeader(it.createdAt.replaceAfter(".", " "))
                }

                item {
                    Message(
                        isUserMe = it.userFrom == me,
                        messageDir = messageDir,
                        onPostcardClick,
                        msg = it,
                        isFirstMessageByAuthor
                    )
                }
                prevAuthor = it.userFrom
            }
        }
        // Jump to bottom button shows up when user scrolls past a threshold.
        // Convert to pixels:
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }

        // Show the button if the first visible item is not the first one or if the offset is
        // greater than the threshold.
        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 ||
                        scrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }

        JumpToBottom(
            // Only show if the scroller is not at the bottom
            enabled = jumpToBottomButtonEnabled,
            onClicked = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun Message(
    isUserMe: Boolean,
    messageDir: String,
    onPostcardClick: (String) -> Unit,
    msg: MessageEntity,
    isFirstMessageByAuthor: Boolean
) {

    val start = if(isUserMe) 80.dp else 10.dp
    val end = if(isUserMe) 10.dp else 80.dp
    Row {
        AuthorAndTextMessage(
            messageDir,
            msg = msg,
            isUserMe = isUserMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp, end = end, start = start)
                .weight(1f),
            onPostcardClick
        )
    }
}

@Composable
fun AuthorAndTextMessage(
    messageDir: String,
    msg: MessageEntity,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    modifier: Modifier = Modifier,
    onPostcardClick: (String) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment =  if(isUserMe) Alignment.End else Alignment.Start
    ) {
        AuthorNameTimestamp(msg)
        ChatItemBubble(messageDir, msg, isUserMe = isUserMe, onPostcardClick)
        if (isFirstMessageByAuthor) {
            // Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

private val ChatBubbleShape = fun(isMe: Boolean) = RoundedCornerShape(
    if(isMe) 20.dp else 4.dp, if(isMe) 4.dp else 20.dp, 20.dp, 20.dp
)

@Composable
fun ChatItemBubble(
    messageDir: String,
    message: MessageEntity,
    isUserMe: Boolean,
    onPostcardClick: (String) -> Unit
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Column(
        Modifier
            .clip(ChatBubbleShape(isUserMe))
            .background(backgroundBubbleColor)
            .padding(15.dp)
    ) {
        ClickableMessage(
            messageDir = messageDir,
            message = message,
            onPostcardClick = onPostcardClick
        )
    }
}

@Composable
fun ClickableMessage(
    messageDir: String,
    message: MessageEntity,
    onPostcardClick: (String) -> Unit
) {
    val context = LocalContext.current
    val path = messageDir + "/" + message.fileName
    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(path)
            .decoderFactory(SvgDecoder.Factory())
            .build(),
        contentDescription = null,
        modifier = Modifier.clickable { onPostcardClick(path) }
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