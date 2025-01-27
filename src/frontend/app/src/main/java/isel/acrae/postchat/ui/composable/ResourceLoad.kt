package isel.acrae.postchat.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource

@Composable
fun loadImageVector(@DrawableRes id : Int) = ImageVector.vectorResource(id = id)

@Composable
fun loadImageBitmap(@DrawableRes id : Int) = ImageBitmap.imageResource(id = id)