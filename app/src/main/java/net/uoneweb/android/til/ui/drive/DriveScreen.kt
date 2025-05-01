package net.uoneweb.android.til.ui.drive

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import net.uoneweb.android.til.R

@Composable
fun DriveScreen() {

    //SigninWithFirebase()
    SigninWithOAuthScreen()

}

@Composable
fun SigninWithFirebase() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column {

        Button(
            onClick = {
                val credentialManager = CredentialManager.create(context)
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(getString(context, R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(true)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()
                coroutineScope.launch {
                    try {
                        val result = credentialManager.getCredential(context, request)
                        handleSignIn(result)
                    } catch (e: GetCredentialException) {
                        Log.e("DriveScreen", "Error: ${e.message}")
                    }
                }
            },
            modifier = Modifier.padding(16.dp),
        ) {
            Text(text = "Login")
        }

        Button(
            {
                val auth = Firebase.auth
                Log.d("DriveScreen", "auth: ${auth.currentUser}")
                auth.getAccessToken(true).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("DriveScreen", "accessToken: ${task.result.token}")
                    } else {
                        Log.d("DriveScreen", "failed: ${task.exception}")
                    }
                }
            },
        ) {
            Text("test")
        }
    }
}

private suspend fun handleSignIn(response: GetCredentialResponse) {
    val credential = response.credential
    if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
    } else {
        Log.w("DriveScreen", "Credential is not of type Google ID!")
    }
}

private suspend fun firebaseAuthWithGoogle(idToken: String) {
    val auth = Firebase.auth
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    val result = auth.signInWithCredentialSuspend(credential)
    if (result != null) {
        // Sign in success, update UI with the signed-in user's information
        Log.d("DriveScreen", "signInWithCredential:success")
        val user = auth.currentUser
        Log.d("DriveScreen", "User: $user")
    } else {
        // If sign in fails, display a message to the user
        Log.w("DriveScreen", "signInWithCredential:failure")
    }
}

suspend fun FirebaseAuth.signInWithCredentialSuspend(credential: AuthCredential): AuthResult? {
    return suspendCancellableCoroutine { continuation ->
        this.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result) { cause, value, context ->
                        Log.w("DriveScreen", "cancelled: ${cause.message}")
                    }
                } else {
                    continuation.resume(null) { cause, value, context ->
                        Log.e("DriveScreen", "failed:: ${cause.message}")
                    }
                }
            }
    }
}