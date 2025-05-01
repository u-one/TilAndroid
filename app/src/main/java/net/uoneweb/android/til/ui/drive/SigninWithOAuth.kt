package net.uoneweb.android.til.ui.drive


import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import java.util.UUID

// Firebaseを使わない場合はたぶんこっちだが、まだうまく動かない
// https://github.com/firebase/snippets-android/blob/master/auth/app/src/main/java/com/google/firebase/quickstart/auth/kotlin/GoogleSignInActivity.kt
/*
    依存パッケージ
    implementation("androidx.credentials:credentials:1.5.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.5.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
*/

@Composable
fun SigninWithOAuthScreen() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var googleCredential by remember { mutableStateOf<GoogleIdTokenCredential?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (googleCredential != null) {
            Text("Signed in as:) ${googleCredential?.displayName}")
        } else {
            Button(
                onClick = {
                    coroutineScope.launch {
                        signIn(
                            context,
                            onSuccess = { credential -> googleCredential = credential },
                            onError = { msg -> errorMessage = "${msg}" },
                        )
                    }
                },
            ) {
                Text("Google Sign-In")
            }
        }
        if (errorMessage.isNotEmpty()) {
            Text(text = "Error: $errorMessage", color = androidx.compose.ui.graphics.Color.Red)
        }
    }
}

private suspend fun signIn(context: Context, onSuccess: (GoogleIdTokenCredential) -> Unit, onError: (String?) -> Unit) {
    val TAG = "DriveScreen"
    val credentialManager = CredentialManager.create(context)

    // project:backflip2709bb, OAuth 2.0クライアント: Androidクライアント1
    val WEB_CLIENT_ID = "797824601618-5fducd7eq7n7l81smb3bud08rdv041em.apps.googleusercontent.com"
    val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption
        .Builder(WEB_CLIENT_ID)
        .setNonce(nonce())
        .build()

    val googleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(true)
        .setServerClientId(WEB_CLIENT_ID)
        .setAutoSelectEnabled(true)
        .setNonce(nonce())
        .build()

    val request: GetCredentialRequest = GetCredentialRequest
        .Builder()
        .addCredentialOption(signInWithGoogleOption)
        .build()

    try {
        val result = credentialManager.getCredential(context, request)
        val credential = handleSignIn(result, onError = { msg -> onError(msg) })
        credential?.let { onSuccess(it) }
    } catch (e: GetCredentialException) {
        Log.e(TAG, "Error: $e")
        onError(e.message)
    }
}

private fun handleSignIn(result: GetCredentialResponse, onError: (String?) -> Unit): GoogleIdTokenCredential? {
    val TAG = "DriveScreen"
    Log.d(TAG, "handleSignIn result: $result")
    val credential = result.credential
    var googleIdTokenCredential: GoogleIdTokenCredential? = null
    when (credential) {
        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    googleIdTokenCredential = GoogleIdTokenCredential
                        .createFrom(credential.data)
                } catch (e: GoogleIdTokenParsingException) {
                    val msg = "Received an invalid google id token response: $e"
                    Log.e(TAG, msg)
                    onError(msg)
                }
            } else {
                val msg = "Unexpected type of credential: ${credential.type}"
                Log.e(TAG, msg)
                onError(msg)
            }
        }

        else -> {
            val msg = "Unexpected type of credential: ${credential.type}"
            Log.e(TAG, msg)
            onError(msg)
        }
    }
    return googleIdTokenCredential
}

fun nonce(): String {
    return UUID.randomUUID().toString()
}