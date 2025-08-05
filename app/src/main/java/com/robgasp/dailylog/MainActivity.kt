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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.robgasp.dailylog.ui.theme.DailyLogTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: LogEditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val time = viewModel.getTime()
        val list = List(40) {
            "${time.hour}: Day $it" to STATIC_COLORS_TEMP[(0..4).random()]
        }
        setContent {
            DailyLogTheme {
                MainScreen(list)
            }
        }
    }
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
        })
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