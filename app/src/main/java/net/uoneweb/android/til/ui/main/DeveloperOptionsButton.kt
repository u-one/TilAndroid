package net.uoneweb.android.til.ui.main

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DeveloperOptionsButton() {
    val context = LocalContext.current
    val enabled = isDeveloperOptionsEnabled(context)
    OpenButton(
        enabled = enabled,
        onClick = {
            val intent = createDeveloperOptionsIntent()
            context.startActivity(intent)
        },
    )
}

private fun isDeveloperOptionsEnabled(context: Context): Boolean {
    return Settings.Secure.getInt(
        context.contentResolver,
        Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
        0,
    ) == 1
}

private fun createDeveloperOptionsIntent(): Intent {
    return Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
}

@Composable
private fun OpenButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    Button(
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
    ) {
        if (enabled) {
            Text("開発者オプション")
        } else {
            Text("開発者オプション無効")
        }
    }
}

@Composable
fun CreateDeveloperOptionsShortcutButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            createAppShortCut(context)
        },
        shape = RoundedCornerShape(4.dp),
    ) {
        Text("ショートカット作成")
    }
}

private fun createAppShortCut(context: Context) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return

    val shortcutManager = context.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager

    val shortcut =
        ShortcutInfo.Builder(context, "developer_options")
            .setShortLabel("開発者オプション")
            .setIcon(Icon.createWithResource(context, android.R.drawable.sym_def_app_icon))
            .setIntent(createDeveloperOptionsIntent())
            .build()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        shortcutManager.requestPinShortcut(shortcut, null)
    }
}

@Composable
@Preview(showBackground = true)
private fun OpenButtonEnabledPreview() {
    OpenButton({})
}

@Composable
@Preview(showBackground = true)
private fun OpenButtonDisabledPreview() {
    OpenButton({}, enabled = false)
}

@Composable
@Preview(showBackground = true)
private fun CreateDeveloperOptionsShortcutButtonPreview() {
    CreateDeveloperOptionsShortcutButton()
}
