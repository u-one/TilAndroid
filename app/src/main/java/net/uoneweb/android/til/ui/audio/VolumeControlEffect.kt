package net.uoneweb.android.til.ui.audio

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun VolumeControlEffect(onVolumeChanged: () -> Unit) {
    val context = LocalContext.current

    if (context !is Activity) {
        return
    }

    val volumeReceiver =
        object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent,
            ) {
                if (intent.action == "android.media.VOLUME_CHANGED_ACTION") {
                    onVolumeChanged()
                }
            }
        }

    DisposableEffect(Unit) {
        val intentFilter = IntentFilter().apply {
            addAction("android.media.VOLUME_CHANGED_ACTION")
        }
        context.registerReceiver(volumeReceiver, intentFilter)
        onDispose {
            context.unregisterReceiver(volumeReceiver)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = context
    DisposableEffect(lifecycleOwner) {
        val observer =
            LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    activity.volumeControlStream = AudioManager.STREAM_DTMF
                }
                if (event == Lifecycle.Event.ON_PAUSE) {
                    activity.volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
                }
            }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            activity.volumeControlStream = AudioManager.USE_DEFAULT_STREAM_TYPE
        }
    }
}
