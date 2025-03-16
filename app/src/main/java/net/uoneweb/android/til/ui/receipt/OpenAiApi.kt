package net.uoneweb.android.til.ui.receipt

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


data class ChatRequest(
    val model: String,
    val messages: List<Message>,
)

data class Message(
    val role: String,
    val content: String,
)

data class ChatResponse(
    val id: String,
    val choices: List<Choice>,
)

data class Choice(
    val message: Message,
)


interface OpenAiApi {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    fun getChatCompletion(@Body request: ChatRequest): Call<ChatResponse>
}