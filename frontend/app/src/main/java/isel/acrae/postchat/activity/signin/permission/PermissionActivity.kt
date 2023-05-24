package isel.acrae.postchat.activity.signin.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import isel.acrae.postchat.activity.signin.SignInActivity
import isel.acrae.postchat.ui.theme.PostChatTheme

class PermissionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionStatus = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        )

        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            return SignInActivity.navigate(this)
        }

        setContent {
                PostChatTheme {

                var permissionGranted by remember {
                    mutableStateOf(isPermissionGranted())
                }

                val permissionLauncher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { permissionGranted_ ->
                        permissionGranted = permissionGranted_
                    }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "In order for this app to work we need permission to read your contacts",
                                fontSize = 20.sp
                            )
                        }

                        Button(
                            enabled = !permissionGranted, // if the permission is NOT granted, enable the button
                            onClick = {
                                if (!permissionGranted) {
                                    // ask for permission
                                    permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                                }
                            }) {
                            Text(text = if (permissionGranted) "Permission Granted" else "Enable Permission")
                        }

                        if (permissionGranted) {
                            // update your UI
                            Toast.makeText(
                                this@PermissionActivity,
                                "Permission granted",
                                Toast.LENGTH_SHORT
                            ).show()
                            SignInActivity.navigate(this@PermissionActivity)
                        }
                    }
                }
            }
        }
    }

    // check initially if the permission is granted
    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

}