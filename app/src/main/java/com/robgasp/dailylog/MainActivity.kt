package com.robgasp.dailylog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.robgasp.dailylog.core.ui.BottomNavPanel
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
import com.robgasp.dailylog.features.logs.LogDetailsViewModel
import com.robgasp.dailylog.navigation.ScreenKey
import com.robgasp.dailylog.util.DoNothing
import com.robgasp.dailylog.util.showDismissableSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

val BottomNavHeight = 104.dp

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
                val vm = hiltViewModel<LogDetailsViewModel, LogDetailsViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(it.logId)
                    }
                )
                LogDetailsScreen(vm = vm)
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

    val showNavigationBar = navigationViewModel.topLevelBackStack.currentKey !is ScreenKey

    Scaffold(
        topBar = {
            TopBar(
                screenTitle = navigationViewModel.topLevelBackStack.currentKey.appBarTitle,
                onAvatarClick = {
                    scope.launch {
                        showDismissableSnackBar(snackbarHostState, "Profile clicked") {
                            DoNothing
                        }
                    }
                },
                onNotificationsClick = {
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
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                NavigationWindow(
                    viewModel = navigationViewModel,
                    modifier = Modifier.padding(
                        top = innerPadding.calculateTopPadding(),
                    )
                )

                AnimatedVisibility(
                    visible = showNavigationBar,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(Color.Transparent),
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    val midIndex = TOP_LEVEL_TABS.size / 2

                    BottomNavPanel(
                        fabContent = {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                        },
                        fabAction = {
                            navigationViewModel.topLevelBackStack.addKey(Create)
                        },
                        leftContent = {
                            NavTabSectionContent(
                                rootKeys = TOP_LEVEL_TABS.subList(0, midIndex),
                                selectedKey = navigationViewModel.topLevelBackStack.tabLevelKey as RootKey,
                                onSelect = { navigationViewModel.topLevelBackStack.addKey(it) }
                            )
                        },
                        rightContent = {
                            NavTabSectionContent(
                                rootKeys = TOP_LEVEL_TABS.subList(midIndex, TOP_LEVEL_TABS.size),
                                selectedKey = navigationViewModel.topLevelBackStack.tabLevelKey as RootKey,
                                onSelect = { navigationViewModel.topLevelBackStack.addKey(it) }
                            )
                        }
                    )
                }
            }
        },
    )
}

@Composable
fun RowScope.NavTabSectionContent(
    rootKeys: List<RootKey>,
    selectedKey: RootKey,
    onSelect: (key: RootKey) -> Unit,
) {
    rootKeys.forEachIndexed { index, rootKey ->
        val isSelected =
            selectedKey == rootKey
        val tab = rootKey.getTab()
        NavigationBarItem(
            selected = isSelected,
            onClick = { onSelect(rootKey) },
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

private fun RootKey.getTab(): Tab {
    return when (this) {
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
