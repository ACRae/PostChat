package isel.acrae.postchat.ui.composable


import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ExpandableFAB(
    modifier: Modifier = Modifier,
    description: String,
    icon: ImageVector,
    leftContent: @Composable (() -> Unit) = {},
    secondaryContent: @Composable (() -> Unit) = {},
    content: @Composable (() -> Unit),
) {
    var isExpanded by remember { mutableStateOf(false) }
    var heightOffset by remember { mutableStateOf(0) }
    val density = Resources.getSystem().displayMetrics.scaledDensity

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.End
    ) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Column(
                Modifier.offset(y = (heightOffset / density).dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                if (isExpanded) Column(
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End
                ) {
                    content.invoke()
                }
                FloatingActionButton(
                    modifier = Modifier.padding(top = 5.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = description
                    )
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .onSizeChanged {//do this to align content
                        heightOffset = it.height
                    }
                    .offset(x = (20).dp)
            ) {
                secondaryContent.invoke()
                Row(
                    Modifier.fillMaxWidth().offset(x = (-80).dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    leftContent()
                }
            }
        }
    }
}


@Composable
fun SmallExpandableFABItem(
    description: String,
    icon: ImageVector,
    onClick: () -> Unit = {}
) {
    Row(
        Modifier.padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .shadow(1.dp, RoundedCornerShape(5.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 7.dp, end = 7.dp, top = 2.dp, bottom = 2.dp)
        ) {
            Text(
                description, fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        SmallIconFab(icon = icon, description, onClick)
    }
}


@Composable
fun SmallIconFab(
    icon: ImageVector,
    description: String? = null,
    onClick: () -> Unit = {}
) {
    SmallFloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Icon(imageVector = icon, contentDescription = description)
    }
}

