package net.uoneweb.android.til.ui.receipt

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileUploadApi {
    @Multipart
    @POST("v1/files")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("purpose") purpose: RequestBody,
    ): Call<FileUploadResponse>
}

data class FileUploadResponse(
    val id: String,
    val objectType: String,
    val purpose: String,
)

