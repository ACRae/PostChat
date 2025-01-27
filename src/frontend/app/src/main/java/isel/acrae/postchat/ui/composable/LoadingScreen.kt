package isel.acrae.postchat.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    CircularProgressIndicator(
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp)
            .wrapContentSize()
    )
}