package com.example.travelapp.ai

import android.content.Context
import java.util.Properties

object ApiKeyProvider {
    fun getGeminiApiKey(context: Context): String {
        val properties = Properties()
        val assetManager = context.assets
        assetManager.open("apikey.properties").use { inputStream ->
            properties.load(inputStream)
        }
        return properties.getProperty("GEMINI_API_KEY") ?: ""
    }
}