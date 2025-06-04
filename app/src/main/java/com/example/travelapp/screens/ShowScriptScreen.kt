import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShowScriptScreen(
    script: String,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onAdjust: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
        Text("Roteiro Gerado:", style = MaterialTheme.typography.titleMedium)
        Text(script, modifier = Modifier.padding(vertical = 16.dp))
        Button(onClick = onAccept, modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Aceitar")
        }
        Button(onClick = onReject, modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Rejeitar")
        }
        Button(onClick = onAdjust, modifier = Modifier.padding(vertical = 4.dp)) {
            Text("Ajustar")
        }
    }
}
