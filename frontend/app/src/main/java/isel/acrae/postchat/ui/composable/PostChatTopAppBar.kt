package isel.acrae.postchat.ui.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import isel.acrae.postchat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostChatTopAppBar(
    title: String? = null,
    action: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        title = {
        Text(
            title ?: stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.background
        )
    },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = { action() }
    )
}
