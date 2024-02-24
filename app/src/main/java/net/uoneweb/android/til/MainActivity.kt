package net.uoneweb.android.til

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.uoneweb.android.til.ui.camera.CameraFragment
import net.uoneweb.android.til.ui.main.MainFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TilApp()
        }

        checkPermissions()
    }

    private fun checkPermissions() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}

@Composable
private fun TilApp() {
    val navController = rememberNavController()
    TilNavHost(navController = navController)
}

@Composable
private fun TilNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = "main") {
        composable("camera") {
            CameraFragmentComposable()
        }
        composable("main") {
            MainFragmentComposable()
        }
        composable("hoge") {
            Hoge()
        }
    }
}

@Composable
private fun MainFragmentComposable() {
    FragmentComposable(MainFragment())
}

@Composable
private fun CameraFragmentComposable() {
    FragmentComposable(CameraFragment())
}

@Composable
private fun FragmentComposable(fragment: Fragment) {
    val fragmentManager = when(val context = LocalContext.current) {
        is FragmentActivity -> context.supportFragmentManager
        else -> null
    }
    AndroidView(factory = { ctx ->
        FragmentContainerView(ctx).apply {
            if (id == View.NO_ID) {
                id = View.generateViewId()

            }
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            fragmentManager?.beginTransaction()?.replace(id, fragment)?.commit()
        }
    })
}

@Composable
private fun Hoge() {
    Text("hoge")
}