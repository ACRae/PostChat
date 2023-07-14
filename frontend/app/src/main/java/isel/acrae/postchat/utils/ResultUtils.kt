package isel.acrae.postchat.utils

import android.content.Context
import androidx.compose.runtime.Composable
import isel.acrae.postchat.ui.composable.ErrorDialog
import isel.acrae.postchat.ui.composable.showToast

@Composable
fun <T> Result<T>.Handle(
    onSuccess: @Composable (T) -> Unit,
    onFailure: @Composable (String) -> Unit = {
        ErrorDialog(error = it)
    }
) {
    if(this.isSuccess)
        onSuccess(this.getOrThrow())
    else {
        onFailure(this.exceptionOrNull().toString())
    }
}

fun <T> Result<T>.handleError(
    context: Context,
    onSuccess: (T) -> Unit,
    onFailure: (String) -> Unit = { showToast(context, it) }
) {
    try {
        onSuccess(this.getOrThrow())
    }catch (e : Exception) {
        onFailure(e.message.toString())
    }
}