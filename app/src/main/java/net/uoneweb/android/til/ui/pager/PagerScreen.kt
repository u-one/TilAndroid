package net.uoneweb.android.til.ui.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        state.update(str1 + "        " + str2)
    }
    Column {
        PagerLcdHeader()
        Row {
            PagerLcdLeft()
            DotMatrixLcd(state.dotMatrixLcdState, modifier.background(Color(0xFF446644)))
        }
    }
}

@Composable
fun PagerLcdHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(Color(0xFF446644))
    )
}

@Composable
fun PagerLcdLeft() {
    Box(
        modifier = Modifier
            .height(160.dp)
            .width(20.dp)
            .background(Color(0xFF446644))
    )
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