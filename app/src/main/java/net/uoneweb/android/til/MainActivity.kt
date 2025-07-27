package net.uoneweb.android.til

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.uoneweb.android.gis.ui.location.LocationScreen
import net.uoneweb.android.gis.ui.map.MapScreen
import net.uoneweb.android.receipt.ui.ReceiptScreen
import net.uoneweb.android.til.data.SettingsDataStore
import net.uoneweb.android.til.ui.audio.AudioScreen
import net.uoneweb.android.til.ui.buttons.ButtonsScreen
import net.uoneweb.android.til.ui.camera.CameraScreen
import net.uoneweb.android.til.ui.drive.DriveScreen
import net.uoneweb.android.til.ui.graphql.GraphQLScreen
import net.uoneweb.android.til.ui.haptic.HapticFeedbackScreen
import net.uoneweb.android.til.ui.main.MainScreen
import net.uoneweb.android.til.ui.pager.PagerScreen
import net.uoneweb.android.til.ui.preference.PreferenceScreen

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
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
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS,
            )
        }
    }

    private fun allPermissionsGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                baseContext, it,
            ) == PackageManager.PERMISSION_GRANTED
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT,
                ).show()
                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun TilApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val settings = SettingsDataStore(context)
    val showBottomBar by settings.showBottomBarFlow.collectAsState(initial = true)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "メニュー",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                )
                screens.forEach { screen ->
                    androidx.compose.material3.NavigationDrawerItem(
                        icon = { Icon(screen.icon, contentDescription = stringResource(screen.resourceId)) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = false,
                        onClick = {
                            navController.navigateLocal(screen.route)
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onMenuClick = { scope.launch { drawerState.open() } },
                )
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomBar(navController)
                }
            },
        ) { innerPadding ->
            TilNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onMenuClick: () -> Unit) {
    androidx.compose.material3.TopAppBar(
        title = { Text("TIL App") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Filled.Menu, contentDescription = "メニュー")
            }
        },
    )
}

@Composable
private fun BottomBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = stringResource(screen.resourceId)) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.isCurrentScreen(screen) ?: false,
                onClick = { navController.navigateLocal(screen.route) },
            )
        }
    }
}

private fun (NavDestination).isCurrentScreen(screen: Screen): Boolean = hierarchy.any { it.route == screen.route }

private fun (NavController).navigateLocal(route: String) {
    navigate(route) {
        popUpTo(graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
private fun (RowScope).BottomBarItem(
    screen: Screen,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {
    NavigationBarItem(
        icon = { Icon(screen.icon, contentDescription = stringResource(screen.resourceId)) },
        label = { Text(stringResource(screen.resourceId)) },
        selected = selected,
        onClick = onClick,
    )
}

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector,
) {
    data object Main : Screen("main", R.string.main, Icons.Filled.Home)

    data object Camera : Screen("camera", R.string.camera, Icons.Filled.AccountBox)

    data object HapticSample : Screen("haptic_sample", R.string.haptic_sample, Icons.Filled.Face)

    data object GraphQL : Screen("graphql", R.string.graphql, Icons.Filled.Face)

    data object Buttons : Screen("buttons", R.string.buttons, Icons.Filled.CheckCircle)

    data object Pager : Screen("pager", R.string.pager, Icons.Filled.AccountBox)

    data object Audio : Screen("audio", R.string.audio, Icons.Filled.PlayArrow)

    data object Receipt : Screen("receipt", R.string.receipt, Icons.Filled.AccountBox)

    data object Drive : Screen("drive", R.string.drive, Icons.Filled.Menu)

    data object Location : Screen("location", R.string.location, Icons.Filled.LocationOn)

    data object Map : Screen("map", R.string.map, Icons.Filled.Place)

    data object Preference : Screen("preference", R.string.preference, Icons.Filled.Check)
}

private val screens =
    listOf(
        Screen.Main,
        Screen.Camera,
        Screen.HapticSample,
        Screen.GraphQL,
        Screen.Buttons,
        Screen.Pager,
        Screen.Audio,
        Screen.Receipt,
        Screen.Drive,
        Screen.Location,
        Screen.Map,
        Screen.Preference,
    )

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
private fun TilNavHost(
    navController: NavHostController,
    modifier: Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Receipt.route,
        modifier = modifier,
    ) {
        composable(Screen.Camera.route) {
            CameraScreen()
        }
        composable(Screen.Main.route) {
            MainScreen()
        }
        composable(Screen.HapticSample.route) {
            HapticFeedbackScreen()
        }
        composable(Screen.GraphQL.route) {
            GraphQLScreen()
        }
        composable(Screen.Buttons.route) {
            ButtonsScreen()
        }
        composable(Screen.Pager.route) {
            PagerScreen()
        }
        composable(Screen.Audio.route) {
            AudioScreen()
        }
        composable(Screen.Receipt.route) {
            ReceiptScreen()
            // ReceiptListScreen()
        }
        composable(Screen.Drive.route) {
            DriveScreen()
        }
        composable(Screen.Location.route) {
            LocationScreen()
        }
        composable(Screen.Map.route) {
            MapScreen()
        }
        composable(Screen.Preference.route) {
            PreferenceScreen()
        }
    }
}
