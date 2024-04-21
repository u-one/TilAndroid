package net.uoneweb.android.til.ui.main

import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView

@Composable
fun MainScreen() {
    FragmentComposable(MainFragment())
}

@Composable
private fun FragmentComposable(fragment: Fragment) {
    val fragmentManager =
        when (val context = LocalContext.current) {
            is FragmentActivity -> context.supportFragmentManager
            else -> null
        }
    AndroidView(factory = { ctx ->
        FragmentContainerView(ctx).apply {
            if (id == View.NO_ID) {
                id = View.generateViewId()
            }
            layoutParams =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
            fragmentManager?.beginTransaction()?.replace(id, fragment)?.commit()
        }
    })
}
