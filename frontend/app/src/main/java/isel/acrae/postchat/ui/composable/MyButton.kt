package isel.acrae.postchat.ui.composable

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MyButton(
    modifier: Modifier = Modifier,
    buttonName: String? = null,
    background: Color = Color.Transparent,
    onClick: () -> Unit,
    content: @Composable () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(4.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(top = 10.dp, start = 15.dp, end = 15.dp, bottom = 10.dp),
        content = {
            if(buttonName != null)
                Text(buttonName, color = MaterialTheme.colorScheme.primary)
            content.invoke()
        }
    )
}