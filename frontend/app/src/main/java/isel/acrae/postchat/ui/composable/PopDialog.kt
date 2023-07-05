package isel.acrae.postchat.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PopDialog(
    onDismiss: (() -> Unit)? = null,
    onConfirm: (() -> Unit)? = null,
    content: @Composable () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {},
        content = {
            Column(
                Modifier
                    .clip(RoundedCornerShape(10))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(20.dp)
            ) {
                content()
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 10.dp, end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if(onDismiss != null) {
                        Button(onClick = { onDismiss() }) {
                            Text("Cancel")
                        }
                    }
                    if(onConfirm != null) {
                        Button(onClick = { onConfirm() }) {
                            Text("Confirm")
                        }
                    }
                }
            }
        }
    )
}