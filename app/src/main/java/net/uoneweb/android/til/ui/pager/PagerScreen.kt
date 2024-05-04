package net.uoneweb.android.til.ui.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.til.ui.pager.lcd.DotMatrixLcd

@Composable
fun PagerScreen(dialPadStateFactory: DialPadStateFactory = DialPadStateFactoryImpl(TonePlayerImpl)) {
    var sentText by remember { mutableStateOf("") }
    var inputText by remember { mutableStateOf("") }
    val dialPadState = remember { dialPadStateFactory.create() }

    VolumeControlEffect()

    Column(modifier = Modifier.fillMaxSize()) {
        PagerLcd(Modifier.fillMaxWidth(), inputText = sentText)
        Spacer(modifier = Modifier.height(20.dp))
        InputText(inputText = inputText, Modifier.fillMaxWidth())
        SoundSwitch(
            checked = dialPadState.playTone,
            onCheckedChange = { dialPadState.playTone = it },
        )
        DialPad(
            onButtonPress = { key ->
                inputText += key

                if (key == '#') {
                    // #の2度押しに対応
                    if (inputText.isNotEmpty() && inputText != "#") {
                        sentText = inputText
                    }
                    inputText = ""
                }
            },
            state = dialPadState,
        )
    }
}

@Composable
private fun InputText(
    inputText: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "メッセージを入力して#を押してください",
            style = MaterialTheme.typography.body1,
            color = Color.Black,
            modifier = Modifier.padding(10.dp),
        )
        OutlinedTextField(
            value = inputText,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
        )
    }
}

@Composable
private fun SoundSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row {
        Text(
            text = "ダイヤル音",
            style = MaterialTheme.typography.body1,
            color = Color.Black,
            modifier = Modifier.padding(10.dp),
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun PagerLcd(
    modifier: Modifier = Modifier,
    inputText: String = "",
) {
    val state = rememberPagerLcdState()
    val str1 = "TOKYO"
    val str2 = "TELEMESSAGE"

    LaunchedEffect(Unit) {
        state.updateWithText(str1 + "        " + str2)
    }
    LaunchedEffect(inputText) {
        state.update(inputText)
    }
    Column(modifier = modifier) {
        Row {
            DotMatrixLcd(state.dotMatrixLcdState, modifier.background(Color(0xFF446644)))
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 500, heightDp = 160)
fun PagerLcdLargePreview(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "1*21*41*61*011",
        )

        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "3*23*43*63*0",
        )
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "4*24*44*64*0",
        )
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "5*25*45*65*0",
        )
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "6*26*46*66*0",
        )
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "7*27*47*67*0",
        )
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "8*28*48*68*0",
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 500, heightDp = 100)
fun PagerLcdLargePreview2(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "2*22*42*62*0",
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 500, heightDp = 1000)
fun PagerLcdPreview(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "*2*2111213141588212223242588883132333435884142434445",
        )
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "515253545588616263646588887172737475888182838485",
        )
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "919293949588010203880405",
        )
        PagerLcd(
            modifier = Modifier.height(160.dp),
            // ABCDEFGHIJKLMNOPQRSTUVWXYZ
            inputText = "1617181910262728292036373839304647484940565758595066",
        )
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "676869607677787970828486878889",
        )
        PagerLcd(
            modifier = Modifier.height(160.dp),
            inputText = "96979899900607080900",
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PagerScreenPreview() {
    PagerScreen(DialPadStateFactoryImpl(DummyTonePlayer()))
}
