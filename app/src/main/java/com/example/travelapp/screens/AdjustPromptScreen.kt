import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelapp.database.AppDatabase
import com.example.travelapp.factory.RegisterTravelListViewModelFactory
import com.example.travelapp.viewmodel.RegisterTravelListViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun AdjustPromptScreen(
    initialPrompt: String,
    travelId: Int,
    onPromptAdjusted: (String) -> Unit,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current
    val travelDao = AppDatabase.getDatabase(ctx).travelDao()
    val registerTravelViewModel: RegisterTravelListViewModel = viewModel(
        factory = RegisterTravelListViewModelFactory(travelDao)
    )
    var prompt by remember { mutableStateOf(initialPrompt) }
    var roteiro by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val navController = rememberNavController()

    Column(modifier = Modifier.padding(16.dp).verticalScroll(scrollState)) {
        Text("Ajuste o prompt para o roteiro:")
        TextField(value = prompt, onValueChange = { prompt = it })
        Button(onClick = {
            isLoading = true
            coroutineScope.launch {
                roteiro = gerarRoteiroComPromptGemini(prompt)
                isLoading = false
                navController.navigate("ShowScriptScreen?script=" + java.net.URLEncoder.encode(roteiro, java.nio.charset.StandardCharsets.UTF_8.toString()) + "&travelId=$travelId")
            }
        }) {
            Text("Gerar Roteiro")
        }
        if (isLoading) Text("Gerando roteiro...")
        Button(onClick = onBack) { Text("Voltar") }
    }
}

// Exemplo de função suspensa para gerar roteiro com prompt customizado
suspend fun gerarRoteiroComPromptGemini(prompt: String): String {
    // Chame a API Gemini com o prompt customizado
    return "Roteiro gerado com prompt customizado: $prompt"
}