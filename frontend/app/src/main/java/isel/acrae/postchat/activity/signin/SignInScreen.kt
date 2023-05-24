package isel.acrae.postchat.activity.signin

import android.content.Context
import android.telephony.TelephonyManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import isel.acrae.postchat.R
import java.util.Locale

@Immutable
enum class SingIn(val text: String, val detail: String) {
    LOGIN("Login", "If you already have an account"),
    REGISTER("Register", "If don't have an account");

    fun change(): SingIn {
        return when (this) {
            LOGIN -> REGISTER
            REGISTER -> LOGIN
        }
    }
}

val phoneUtil: PhoneNumberUtil by lazy { PhoneNumberUtil.getInstance() }


@Composable
fun SignInScreen(
    onLogin: (String, Int, String) -> Unit = {_,_,_ -> },
    onRegister: (String, String, Int, String) -> Unit = {_,_,_,_ -> }
) {
    val context = LocalContext.current
    var signin by remember { mutableStateOf(SingIn.LOGIN) }
    var password by remember { mutableStateOf("") }
    var region by remember { mutableStateOf(getCountryMobileCode(context) ?: "") }
    var number by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }


    Column(
        Modifier
            .fillMaxSize()
            .background(
                MaterialTheme
                    .colorScheme
                    .secondaryContainer
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 20.dp),
            text = stringResource(id = R.string.app_name),
            fontSize = 40.sp
        )

        Column(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(
                    MaterialTheme
                        .colorScheme
                        .background
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (signin) {
                SingIn.LOGIN -> Login(
                    region, { region = it },
                    number, { number = it },
                    password, { password = it }
                )
                SingIn.REGISTER -> Register(
                    name, { name = it },
                    region, { region = it },
                    number, { number = it },
                    password, { password = it },
                )
            }

            OutlinedButton(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    when(signin) {
                        SingIn.LOGIN -> onLogin(number, region.toInt(), password)
                        SingIn.REGISTER -> onRegister(name, number, region.toInt(), password)
                    }
                }
            ) {
                Text(text = signin.text)
            }
            Row(Modifier.padding(5.dp)) {
                Text(
                    signin.change().detail + " - ",
                    fontSize = 13.sp
                )
                Text(
                    modifier = Modifier.clickable {
                        signin = signin.change()
                    },
                    text = (signin.change().text),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun SignInScreenPreview() {
    SignInScreen()
}


@Composable
fun Register(
    nameValue: String,
    onNameChange: (String) -> Unit,
    regionValue: String,
    onRegionChange: (String) -> Unit,
    numberValue: String,
    onNumberChange: (String) -> Unit,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
) {
    val name = stringResource(id = (R.string.signin_name))
    val bio = stringResource(id = (R.string.signin_bio))

    BasicOutlinedTextField(label = name, value = nameValue, onValueChange = onNameChange)

    PhoneTextField(
        regionValue = regionValue,
        onRegionChange = onRegionChange,
        numberValue = numberValue,
        onNumberChange = onNumberChange
    )

    PasswordTextField(value = passwordValue, onValueChange = onPasswordChange)
}

@Composable
fun Login(
    regionValue: String,
    onRegionChange: (String) -> Unit,
    numberValue: String,
    onNumberChange: (String) -> Unit,
    passwordValue: String,
    onPasswordChange: (String) -> Unit,
) {
    PhoneTextField(
        regionValue = regionValue,
        onRegionChange = onRegionChange,
        numberValue = numberValue,
        onNumberChange = onNumberChange
    )

    PasswordTextField(value = passwordValue, onValueChange = onPasswordChange)
}


@Composable
fun BasicOutlinedTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        label = {
            Text(text = label)
        },
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
    )
}


@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
) {
    var passwordVisibility by remember { mutableStateOf(false) }
    val visibilityIcon = if (passwordVisibility)
        Icons.Filled.Visibility else Icons.Filled.VisibilityOff

    val errorPassword = value.isNotBlank() && !value.isValidPassword()

    //String resources
    val password = stringResource(id = (R.string.signin_password))
    val passwordErrorString = if(value.length !in 4..30)
        stringResource(id = (R.string.signin_invalid_password_length))
    else stringResource(id = (R.string.signin_invalid_password_letters))

    OutlinedTextField(
        label = {
            Text(
                text = if (!errorPassword) password
                else passwordErrorString
            )
        },
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else
            PasswordVisualTransformation(),
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = visibilityIcon, contentDescription = null)
            }
        },
        isError = errorPassword
    )
}

internal fun String.isValidPassword() =
    isNotBlank() &&
            length in 4..30 &&
            any { it.isUpperCase() } &&
            any { it.isDigit() }

@Composable
fun PhoneTextField(
    regionValue: String,
    onRegionChange: (String) -> Unit,
    numberValue: String,
    onNumberChange: (String) -> Unit,
) {
    //Error
    val errorRegion = regionValue.isNotBlank() && !regionValue.isDigitsOnly()
    val errorNumber = numberValue.isNotBlank() &&
            !isValidPhoneNumber(numberValue, regionValue.toIntOrNull())

    //String resources
    val region = stringResource(id = R.string.signin_region)
    val invalid = stringResource(id = R.string.invalid)
    val number = stringResource(id = R.string.signin_number)

    Row(modifier = Modifier.width(280.dp)) {
        OutlinedTextField(
            value = regionValue,
            onValueChange = onRegionChange,
            label = {
                Text(
                    text = if (!errorRegion) region
                    else invalid
                )
            },
            modifier = Modifier
                .weight(0.5f)
                .padding(end = 5.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = errorRegion
        )

        OutlinedTextField(
            value = numberValue,
            onValueChange = onNumberChange,
            label = {
                Text(
                    text = if (!errorNumber) number
                    else invalid
                )
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = errorNumber
        )
    }
}

fun getCountryMobileCode(context: Context): String? {
    return try {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkCountryIso = telephonyManager.networkCountryIso
        return phoneUtil.getCountryCodeForRegion(
            networkCountryIso.uppercase(Locale.ROOT)
        ).toString()
    } catch (e: Exception) {
        null
    }
}

fun isValidPhoneNumber(phoneNumber: String, region: Int?): Boolean {
    region ?: return false
    val number = try {
        phoneUtil.parse(
            phoneNumber,
            phoneUtil.getRegionCodeForCountryCode(region)
        )
    } catch (e: NumberParseException) {
        return false
    }
    return phoneUtil.isValidNumber(number)
}
