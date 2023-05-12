package isel.acrae.postchat.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun ToolbarMenu(
    child : @Composable (RowScope.() -> Unit)
) {
    Row(
        Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
            .shadow(1.dp, RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(9.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        content = child
    )
}