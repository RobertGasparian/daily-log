package com.robgasp.dailylog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.robgasp.dailylog.screens.NavigationViewModel
import com.robgasp.dailylog.screens.Screen
import com.robgasp.dailylog.screens.ScreenA
import com.robgasp.dailylog.screens.ScreenB
import com.robgasp.dailylog.screens.ScreenC
import com.robgasp.dailylog.ui.theme.DailyLogTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: LogEditorViewModel by viewModels()
    val navViewModel: NavigationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val time = viewModel.getTime()
        val list = List(40) {
            "${time.hour}: Day $it" to STATIC_COLORS_TEMP[(0..4).random()]
        }
        setContent {
            DailyLogTheme {
                val backstack = remember { navViewModel.backStack.toMutableStateList() }
                NavigationWindow(backstack, viewModel)
            }
        }
    }
}

@Composable
fun NavigationWindow(backStack: SnapshotStateList<Screen>, viewModel: LogEditorViewModel, modifier: Modifier = Modifier) {
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Screen.A> {
                ScreenA {
                    backStack.add(Screen.B(it))
                }
            }
            entry<Screen.B> {
                ScreenB(it.id) {
                    backStack.add(Screen.C(viewModel.getLog(it.id)))
                }
            }
            entry<Screen.C> {
                ScreenC(it.log) {
                    Snapshot.withMutableSnapshot {
                        backStack.clear()
                        backStack.add(Screen.A)
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(list: List<Pair<String, Color>>) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(title = { Title() })
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
//        bottomBar = {
//            BottomAppBar {
//                Text("Bottom Bar")
//            }
//        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar("FAB Clicked")
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        content = { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                DaysListScreen(
                    list,
                )
            }
        }
    )
}

val STATIC_COLORS_TEMP = arrayOf(
    Color.Red,
    Color.Blue,
    Color.Yellow,
    Color.Green,
    Color.Gray
)

@Composable
fun Title(modifier: Modifier = Modifier) {
    Text(
        text = "Daily Log",
        modifier = modifier,
        fontSize = 32.sp
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DailyLogTheme {
        MainScreen(
            listOf(
                "Day 1" to Color.Red,
                "Day 2" to Color.Blue,
                "Day 3" to Color.Yellow,
                "Day 4" to Color.Green,
            )
        )
    }
}
