package com.robgasp.dailylog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.robgasp.dailylog.navigation.Create
import com.robgasp.dailylog.navigation.NavigationViewModel
import com.robgasp.dailylog.features.logs.LogsScreen
import com.robgasp.dailylog.navigation.Insights
import com.robgasp.dailylog.navigation.Logs
import com.robgasp.dailylog.features.create.CreateScreen
import com.robgasp.dailylog.features.create.CreateViewModel
import com.robgasp.dailylog.features.insights.InsightsScreen
import com.robgasp.dailylog.features.insights.InsightsViewModel
import com.robgasp.dailylog.features.logs.LogDetailsScreen
import com.robgasp.dailylog.features.logs.LogsViewModel
import com.robgasp.dailylog.navigation.LogDetails
import com.robgasp.dailylog.navigation.RootKey
import com.robgasp.dailylog.navigation.TOP_LEVEL_TABS
import com.robgasp.dailylog.ui.theme.DailyLogTheme
import com.robgasp.dailylog.core.ui.Tab
import com.robgasp.dailylog.core.ui.TopBar
import com.robgasp.dailylog.util.DoNothing
import com.robgasp.dailylog.util.showDismissableSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyLogTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun NavigationWindow(modifier: Modifier = Modifier, viewModel: NavigationViewModel = viewModel()) {
    val logsVM: LogsViewModel = hiltViewModel()
    val createVM: CreateViewModel = hiltViewModel()
    val insightsVM: InsightsViewModel = hiltViewModel()
    NavDisplay(
        modifier = modifier,
        backStack = viewModel.topLevelBackStack.backStack,
        onBack = { viewModel.topLevelBackStack.removeLast() },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<Logs> {
                LogsScreen(vm = logsVM) {
                    viewModel.topLevelBackStack.addKey(LogDetails(it))
                }
            }
            entry<Create> {
                CreateScreen(vm = createVM)
            }
            entry<Insights> {
                InsightsScreen(insightsVM)
            }
            entry<LogDetails> {
                LogDetailsScreen(viewModel = hiltViewModel())
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val navigationViewModel: NavigationViewModel = viewModel()
    Scaffold(
        topBar = {
            TopBar(
                screenTitle = navigationViewModel.topLevelBackStack.topLevelKey.appBarTitle,
                onAvatarClick = {
//                    navigationViewModel.openProfile()
                    scope.launch {
                        showDismissableSnackBar(snackbarHostState, "Profile clicked") {
                            DoNothing
                        }
                    }
                },
                onNotificationsClick = {
//                    navigationViewModel.openNotifications()
                    scope.launch {
                        showDismissableSnackBar(snackbarHostState, "Notifications clicked") {
                            DoNothing
                        }
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        bottomBar = {
            NavigationBar {
                TOP_LEVEL_TABS.forEach { rootKey ->
                    val isSelected = navigationViewModel.topLevelBackStack.topLevelKey == rootKey
                    val tab = rootKey.getTab()
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { navigationViewModel.topLevelBackStack.addKey(rootKey) },
                        icon = {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.label
                            )
                        },
                        label = {
                            Text(tab.label)
                        }
                    )
                }
            }
        },
//        floatingActionButton = {
//            FloatingActionButton(onClick = {
//                scope.launch {
//                    snackbarHostState.showSnackbar("FAB Clicked")
//                }
//            }) {
//                Icon(Icons.Default.Add, contentDescription = "Add")
//            }
//        },
        content = { innerPadding ->
            NavigationWindow(
                viewModel = navigationViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    )
}

private fun NavigationViewModel.openNotifications() {
    // navigate to notifications screen
}

private fun NavigationViewModel.openProfile() {
    // navigate to profile screen
}

private fun RootKey.getTab(): Tab {
    return when (this) {
        Create -> Tab.CREATE
        Insights -> Tab.INSIGHTS
        Logs -> Tab.LOGS
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DailyLogTheme {
        MainScreen()
    }
}
