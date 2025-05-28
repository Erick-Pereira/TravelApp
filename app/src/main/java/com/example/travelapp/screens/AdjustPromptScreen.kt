import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun AdjustPromptScreen(
    initialPrompt: String,
    travelId: Int,
    onPromptAdjusted: (String) -> Unit,
    onBack: () -> Unit
) {
    var prompt by remember { mutableStateOf(initialPrompt) }
    var roteiro by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Ajuste o prompt para o roteiro:")
        TextField(value = prompt, onValueChange = { prompt = it })
        Button(onClick = {
            isLoading = true
            coroutineScope.launch {
                roteiro = gerarRoteiroComPromptGemini(prompt)
                isLoading = false
            }
        }) {
            Text("Gerar Roteiro")
        }
        if (isLoading) Text("Gerando roteiro...")
        if (roteiro.isNotBlank()) {
            Text("Novo roteiro:")
            Text(roteiro)
            Button(onClick = { onPromptAdjusted(roteiro) }) {
                Text("Aceitar Roteiro")
            }
        }
        Button(onClick = onBack) { Text("Voltar") }
    }
}

// Exemplo de função suspensa para gerar roteiro com prompt customizado
suspend fun gerarRoteiroComPromptGemini(prompt: String): String {
    // Chame a API Gemini com o prompt customizado
    return "Roteiro gerado com prompt customizado: $prompt"
}