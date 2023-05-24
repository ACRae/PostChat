package isel.acrae.postchat.ui.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import isel.acrae.postchat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostChatTopAppBar(
    action: @Composable() (RowScope.() -> Unit)
) {
    TopAppBar(
        title = {
        Text(
            stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.background
        )
    },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = { action() }
    )
}
