package net.uoneweb.android.til.ui.receipt

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


data class ChatRequest(
    val model: String,
    val temperature: Double? = null,
    val messages: List<Message>,
    val response_format: ResponseFormat? = null,
)

data class ResponseFormat(
    val type: String,
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

data class FileUploadResponse(
    val id: String,
    val objectType: String,
    val purpose: String,
)


interface OpenAiApi {
    @Headers("Content-Type: application/json")
    @POST("chat/completions")
    fun getChatCompletion(@Body request: ChatRequest): Call<ChatResponse>

    @Multipart
    @POST("v1/files")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("purpose") purpose: RequestBody,
    ): Call<FileUploadResponse>
}


