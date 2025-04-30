package net.uoneweb.android.til.ui.preference

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import net.uoneweb.android.til.data.SettingsDataStore


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferenceScreen() {
    val context = LocalContext.current
    val settingDataStore = remember { SettingsDataStore(context) }
    val apiKey by settingDataStore.preferenceFlow.collectAsState(initial = "")
    val coroutineScope = rememberCoroutineScope()

    Preferences(apiKey) {
        coroutineScope.launch { settingDataStore.saveOpenApiKey(it, context) }
    }
}

@Composable
fun Preferences(apiKey: String, onApiKeyChange: (String) -> Unit) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // APIキー入力用のテキストフィールド
        Text(
            text = "OpenAI API key",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        OutlinedTextField(
            value = apiKey,
            onValueChange = onApiKeyChange,
            label = { Text("API Key") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = !isPasswordVisible,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if (isPasswordVisible) {
                    Icons.Filled.Face
                } else {
                    Icons.Filled.Lock
                }
                IconButton(
                    onClick = {
                        isPasswordVisible = !isPasswordVisible
                    },
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "visibility",
                    )
                }
            },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 現状定常的に保存
        /*
        Button(
            onClick = {
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Save")
        }
         */
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPreferences() {
    Preferences("1234567890") {

    }
}