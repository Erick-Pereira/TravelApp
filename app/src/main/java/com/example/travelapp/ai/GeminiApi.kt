package com.example.travelapp.ai

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun gerarRoteiroComGemini(
    context: Context,
    destino: String,
    tipo: String,
    dataInicio: java.util.Date,
    dataFim: java.util.Date,
    orcamento: Double
): String = withContext(Dispatchers.IO) {
    val apiKey = ApiKeyProvider.getGeminiApiKey(context)
    val generativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = apiKey
    )

    val prompt = """
        Crie um roteiro de viagem detalhado para:
        Destino: $destino
        Tipo de viagem: $tipo
        Data de início: $dataInicio
        Data de término: $dataFim
        Orçamento: R$ $orcamento
        O roteiro deve ser prático, objetivo e considerar o orçamento informado.
    """.trimIndent()

    try {
        val response = generativeModel.generateContent(content { text(prompt) })
        response.text ?: "Não foi possível gerar o roteiro."
    } catch (e: Exception) {
        "Erro ao gerar roteiro: ${e.localizedMessage}"
    }
}