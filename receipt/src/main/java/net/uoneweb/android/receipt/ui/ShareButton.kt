package net.uoneweb.android.receipt.ui

import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TextShareButton(title: String, text: String) {
    val context = LocalContext.current
    Button(
        onClick = {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                putExtra(Intent.EXTRA_SUBJECT, title)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        },
    ) {
        Text("Share")
    }
}


@Composable
@Preview(showBackground = true, widthDp = 320)
fun TextShareButtonPreview() {
    TextShareButton("", "")
}