package net.uoneweb.android.til

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.VibratorManager
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import net.uoneweb.android.til.ui.camera.CameraFragment
import net.uoneweb.android.til.ui.main.MainFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TilApp()
            }
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
    Scaffold(bottomBar = {
        BottomBar(navController)
    }) {innerPadding ->
        TilNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Main : Screen("main", R.string.main, Icons.Filled.Home)
    object Camera : Screen("camera", R.string.camera, Icons.Filled.AccountBox)

    object HapticSample : Screen("haptic_sample", R.string.haptic_sample, Icons.Filled.Face)
}

private val screens = listOf(
    Screen.Main,
    Screen.Camera,
    Screen.HapticSample,
)

@Composable
private fun BottomBar(navController: NavController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach {screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = stringResource(screen.resourceId)) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any {it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

    }
}

@Composable
private fun TilNavHost(
    navController: NavHostController,
    modifier: Modifier
) {
    NavHost(navController = navController, startDestination = Screen.HapticSample.route, modifier = modifier) {
        composable(Screen.Camera.route) {
            CameraFragmentComposable()
        }
        composable(Screen.Main.route) {
            MainFragmentComposable()
        }
        composable(Screen.HapticSample.route) {
            HapticFeedbackScreen()
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
private fun HapticFeedbackScreen() {
    LazyColumn {
        item {
            PredefinedVibration("Click", VibrationEffect.EFFECT_CLICK)
            PredefinedVibration("Double Click", VibrationEffect.EFFECT_DOUBLE_CLICK)
            PredefinedVibration("Heavy Click", VibrationEffect.EFFECT_HEAVY_CLICK)
            PredefinedVibration("Tick", VibrationEffect.EFFECT_TICK)
        }
    }

}

@Composable
private fun PredefinedVibration(text: String, effectId: Int) {
    val context = LocalContext.current
    Text(text = text,
        style = MaterialTheme.typography.body1,
        modifier = Modifier.clickable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                val vibrator = vibratorManager.defaultVibrator
                vibrator.vibrate(VibrationEffect.createPredefined(effectId))
            }
        }
    )
}

@Preview(
    showBackground = true,
)
@Composable
private fun PredefinedVibrationPreview() {
    MaterialTheme {
        PredefinedVibration("Click", VibrationEffect.EFFECT_CLICK)
    }
}