package net.uoneweb.android.til.ui.receipt

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.storage


class ReceiptViewModel(application: Application) : AndroidViewModel(application) {
    private val _uploadedFileUrl: MutableState<Uri?> = mutableStateOf(null)
    val uploadedFileUrl: State<Uri?> = _uploadedFileUrl


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
}