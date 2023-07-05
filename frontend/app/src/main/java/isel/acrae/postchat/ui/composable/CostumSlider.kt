package isel.acrae.postchat.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MySlider() {
    val sliderPosition = remember { mutableStateOf(0.5f) } // Initial value

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Value: ${sliderPosition.value.toInt()}")
        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = sliderPosition.value,
            onValueChange = { newValue ->
                sliderPosition.value = newValue
            },
            modifier = Modifier.fillMaxWidth(),
            steps = 5
        )
    }
}