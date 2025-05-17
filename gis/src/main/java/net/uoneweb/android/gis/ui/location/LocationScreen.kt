package net.uoneweb.android.gis.ui.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun LocationScreen() {
    // gradleに以下の依存関係が必要
    // implementation 'com.google.android.gms:play-services-location:21.3.0'

    // AndroidManifest.xmlに以下が必要
    // <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    // <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />

    var location by remember { mutableStateOf<Location?>(null) }

    CurrentLocationComponent(location) {
        location = it
    }
}
