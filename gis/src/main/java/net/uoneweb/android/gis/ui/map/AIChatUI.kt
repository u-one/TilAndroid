package net.uoneweb.android.gis.ui.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

@Composable
fun AIChatUI(
    userMessage: String,
    onUserMessageChange: (String) -> Unit,
) {
    var isLoading by remember { mutableStateOf(false) }
    var aiResponse by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        if (aiResponse.isNotEmpty()) {
            Text(
                text = "AI: $aiResponse",
                modifier = Modifier.padding(4.dp),
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = userMessage,
                onValueChange = onUserMessageChange,
                label = { Text("AIに質問...") },
                modifier = Modifier.weight(1f),
            )
            Button(
                onClick = {
                    if (userMessage.isNotBlank()) {
                        isLoading = true
                        aiResponse = ""
                        CoroutineScope(Dispatchers.IO).launch {
                            val response = sendMessageToOpenAI(userMessage)
                            aiResponse = response
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading && userMessage.isNotBlank(),
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text(if (isLoading) "送信中..." else "送信")
            }
        }
    }
}

// OpenAI Chat Completions API呼び出し
suspend fun sendMessageToOpenAI(message: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val apiKey = "YOUR_OPENAI_API_KEY" // 実際のAPIキーに置き換えてください

            val url = URL("https://api.openai.com/v1/chat/completions")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val requestBody = JSONObject().apply {
                put("model", "gpt-3.5-turbo")
                put(
                    "messages",
                    org.json.JSONArray().apply {
                        put(
                            JSONObject().apply {
                                put("role", "user")
                                put("content", message)
                            },
                        )
                    },
                )
                put("max_tokens", 150)
                put("temperature", 0.7)
            }

            val writer = OutputStreamWriter(connection.outputStream)
            writer.write(requestBody.toString())
            writer.flush()
            writer.close()

            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = reader.readText()
            reader.close()

            val json = JSONObject(response)
            val choices = json.getJSONArray("choices")
            if (choices.length() > 0) {
                val choice = choices.getJSONObject(0)
                val messageObj = choice.getJSONObject("message")
                return@withContext messageObj.getString("content").trim()
            }

            "レスポンスを取得できませんでした"

        } catch (e: Exception) {
            "エラーが発生しました: ${e.message}"
        }
    }
}