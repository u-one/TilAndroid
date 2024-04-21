package net.uoneweb.android.til.ui.pager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun PagerScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PagerLcd(Modifier.height(160.dp))
    }
}


@Composable
fun PagerLcd(modifier: Modifier = Modifier) {
    val state = rememberPagerLcdState()
    val str1 = "TOKYO"
    val str2 = "TELEMESSAGE"

    LaunchedEffect(Unit) {
        state.update(str2)
    }
    DotMatrixLcd(state.dotMatrixLcdState, modifier)
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
fun PagerLcdPreview() {
    PagerLcd()
}

@Composable
@Preview(showBackground = true, widthDp = 320, heightDp = 160)
fun PagerScreenPreview() {
    PagerScreen()
}