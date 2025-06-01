package net.uoneweb.android.til.ui.pager

import android.app.Activity
import android.media.AudioManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun VolumeControlEffect() {
    val activity = LocalContext.current

    if (activity !is Activity) {
        return
    }

    val lifecycleOwner = LocalLifecycleOwner.current
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
