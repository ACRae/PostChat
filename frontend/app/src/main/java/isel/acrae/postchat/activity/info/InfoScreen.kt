package isel.acrae.postchat.activity.info

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import isel.acrae.postchat.R
import isel.acrae.postchat.ui.composable.loadImageVector

@Composable
fun InfoScreen(
    onOpenUrlRequested: (Uri) -> Unit = { },
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            Authors(
                onOpenUrlRequested
            )
        }
    }
}


@Composable
fun Authors(onOpenUrlRequested: (Uri) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().offset(y = (-60).dp)
    ) {
        Text(
            text = "Made with ❤ by:",
            style = MaterialTheme.typography.headlineMedium
        )
        MySpacer()
        Developer(name = "António Carvalho", "https://github.com/ACRae", onOpenUrlRequested)
        MySpacer(30.dp)
    }
}

@Composable
fun MySpacer(size : Dp = 10.dp){
    Spacer(modifier = Modifier.size(size))
}


@Composable
fun Developer(name : String, link : String = "", onOpenUrlRequested : (Uri) -> Unit){
    val uri = Uri.parse(link)
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .clickable { onOpenUrlRequested(uri) }
            .fillMaxWidth()
            .offset(x = 20.dp)
            .padding(10.dp)
    )
    {
        Icon(
            imageVector = loadImageVector(id = R.drawable.github_mark),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
        MySpacer()
        Text(text = name, style = MaterialTheme.typography.headlineMedium)
    }
}
