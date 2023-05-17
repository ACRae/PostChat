package isel.acrae.postchat.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun SignInScreen() {
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PostChat",
            fontSize = 20.sp
        )
        PhoneTextField(value = phoneNumber, onValueChange = { phoneNumber = it})

        PasswordTextField(value = password, onValueChange = { password = it })

    }
}

@Preview
@Composable
fun SignInScreenPreview() {
    SignInScreen()
}


@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        label = {
           Text(text = "Password")
        },
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else
            PasswordVisualTransformation(),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                val visibilityIcon = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                Icon(imageVector = Icons.Default.Visibility, contentDescription = null)
            }
        },
    )
}

@Composable
fun PhoneTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        label = {
            Text(text = "Phone number")
        },
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
    )
}

