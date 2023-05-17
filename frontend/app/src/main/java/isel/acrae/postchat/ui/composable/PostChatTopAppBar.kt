package isel.acrae.postchat.ui.composable

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
fun PostChatTopAppBar() {
    TopAppBar(
        title = {
        Text(stringResource(id = R.string.app_name))
    },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        actions = {
            Icon(
                modifier = Modifier.offset((-10).dp),
                imageVector = Icons.Default.Info, contentDescription = null
            )
        }
    )
}
