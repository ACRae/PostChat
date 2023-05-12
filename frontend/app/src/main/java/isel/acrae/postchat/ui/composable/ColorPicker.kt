package isel.acrae.postchat.ui.composable

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.sp
import isel.acrae.postchat.draw.utils.PaintProperties


val colors = setOf(
    Color.Black,
    Color.Gray,
    Color.White,
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Yellow,
    Color.Magenta
)

@Composable
fun ColorPicker(
    paint: () -> PaintProperties,
    setPaint: (PaintProperties) -> Unit
) {
    var show by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(paint().color) }

    SmallExpandableFABItem(
        icon = Icons.Default.Add,
        description = "Pick a color",
        onClick = { show = true },
    )

    if (show) {
        AlertDialog(
            onDismissRequest = { show = false }, confirmButton = {
            VerticalGrid(
                Modifier.fillMaxWidth(),
            ) {
                MyButton(
                    buttonName = "Cancel",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { show = false },
                )
                MyButton(
                    buttonName = "Accept",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        setPaint(
                            PaintProperties(
                                stroke = paint().stroke,
                                color = selectedColor,
                                blendMode = DrawScope.DefaultBlendMode,
                                alpha = 1f
                            )
                        )
                        show = false
                    },
                )
            }
        }, text = {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(13.dp), text = "Pick a color", fontSize = 17.sp
                )
                LazyRow(
                    modifier = Modifier.padding(10.dp)
                ) {
                    colors.forEach {
                        item {
                            Box(modifier = Modifier
                                .size(58.dp)
                                .padding(6.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(it)
                                .clickable { selectedColor = it })
                        }
                    }
                }

                VerticalGrid(
                    Modifier.padding(top = 10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Your color", fontSize = 17.sp)
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp)
                                .padding(bottom = 6.dp, start = 6.dp, end = 6.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(paint().color)
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("New color", fontSize = 17.sp)
                        Box(
                            modifier = Modifier
                                .width(100.dp)
                                .height(50.dp)
                                .padding(bottom = 6.dp, start = 6.dp, end = 6.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(selectedColor)
                        )
                    }
                }
            }
        })
    }
}