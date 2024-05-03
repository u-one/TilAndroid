package net.uoneweb.android.til.ui.audio

import android.content.Context
import android.media.AudioManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Stable
data class VolumeInfo(val type: Int, val volume: Int, val min: Int, val max: Int)

@Stable
data class VolumeInfoList(val list: List<VolumeInfo>)

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AudioScreen() {
    val context = LocalContext.current
    val audioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    val streamTypes =
        mapOf(
            AudioManager.STREAM_VOICE_CALL to "Voice Call",
            AudioManager.STREAM_SYSTEM to "System",
            AudioManager.STREAM_RING to "Ring",
            AudioManager.STREAM_MUSIC to "Music",
            AudioManager.STREAM_ALARM to "Alarm",
            AudioManager.STREAM_NOTIFICATION to "Notification",
            AudioManager.STREAM_DTMF to "DTMF",
            AudioManager.STREAM_ACCESSIBILITY to "Accessibility",
        )
    var volumeInfoList by remember {
        mutableStateOf(
            updateVolumeInfoList(audioManager, streamTypes),
        )
    }

    VolumeControlEffect(
        onVolumeChanged = {
            volumeInfoList = updateVolumeInfoList(audioManager, streamTypes)
        },
    )

    LazyColumn {
        items(volumeInfoList.list) { volumeInfo ->
            Text(text = "${streamTypes[volumeInfo.type]}: ${volumeInfo.volume} (${volumeInfo.min}-${volumeInfo.max})")
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
private fun updateVolumeInfoList(
    audioManager: AudioManager,
    streamTypes: Map<Int, String>,
): VolumeInfoList {
    return VolumeInfoList(
        streamTypes.keys.map { streamType ->
            VolumeInfo(
                streamType,
                audioManager.getStreamVolume(streamType),
                audioManager.getStreamMinVolume(streamType),
                audioManager.getStreamMaxVolume(streamType),
            )
        },
    )
}

@RequiresApi(Build.VERSION_CODES.P)
@Preview(showBackground = true)
@Composable
fun AudioScreenPreview() {
    AudioScreen()
}
