package net.uoneweb.android.til.ui.receipt

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI


class ReceiptViewModel(application: Application) : AndroidViewModel(application) {
    private val _uploadedFileUrl: MutableState<Uri?> = mutableStateOf(null)
    val uploadedFileUrl: State<Uri?> = _uploadedFileUrl
    private val _json: MutableState<String> = mutableStateOf("")
    val json: State<String> = _json
    private val _receipt: MutableState<Receipt> = mutableStateOf(Receipt.Empty)
    val receipt: State<Receipt> = _receipt

    fun reset() {
        _uploadedFileUrl.value = null
        _json.value = ""
        _receipt.value = Receipt.Empty
    }

    fun uploadImage(localFileUri: Uri?) {
        if (localFileUri == null) {
            return
        }

        val storage = Firebase.storage
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images")
        val fileRef = imagesRef.child(localFileUri.lastPathSegment!!)

        val uploadTask = fileRef.putFile(localFileUri)
        uploadTask.addOnSuccessListener {
            println("Upload successful")
        }.addOnFailureListener {
            println("Upload failed")
        }

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            fileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                _uploadedFileUrl.value = downloadUri
                println("downloadUri: $downloadUri")
            } else {
                println("downloadUri: failed")
            }
        }
    }

    suspend fun generateJsonFromImage(localFileUri: Uri) {
        val contentResolver = getApplication<Application>().contentResolver
        val bitmap: Bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(localFileUri))
        val prompt = content {
            image(bitmap)
            text(GeminiReceiptPrompt.text)
        }

        val model = Firebase.vertexAI.generativeModel("gemini-2.0-flash")
        val response = model.generateContent(prompt)
        print(response.text)
        val parser = GeminiReceiptResponse(response.text)
        print(parser.json())
        _json.value = parser.json()
        _receipt.value = Receipt(parser.json())
    }


}