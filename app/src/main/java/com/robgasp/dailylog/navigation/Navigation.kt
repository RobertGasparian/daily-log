package com.robgasp.dailylog.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlinx.serialization.Serializable


sealed interface NavKey {
    val appBarTitle: String
}

sealed interface RootKey : NavKey

sealed interface LogsKey : NavKey

sealed interface CreateKey : NavKey

sealed interface InsightsKey : NavKey

// TODO: refactor the hardcoded strings to resources
@Serializable
data object Logs : LogsKey, RootKey {
    override val appBarTitle: String
        get() = "Logs List"
}

@Serializable
data object Create : CreateKey, RootKey {
    override val appBarTitle: String
        get() = "Create New Log"
}

@Serializable
data object Insights : InsightsKey, RootKey {
    override val appBarTitle: String
        get() = "Insights"
}

@Serializable
data class LogDetails(
    val logId: String,
) : LogsKey {
    override val appBarTitle: String
        get() = "Log Details"
}

val TOP_LEVEL_TABS: List<RootKey> = listOf(
    Logs,
    Insights,
    Create
)


class TopLevelBackStack<T : NavKey>(startKey: T) {
    // Maintain a stack for each top level route
    private var topLevelStacks: LinkedHashMap<T, SnapshotStateList<T>> = linkedMapOf(
        startKey to mutableStateListOf(startKey)
    )

    // Expose the current top level route for consumers (selected tab)
    var topLevelKey by mutableStateOf(startKey)
        private set

    // Expose the back stack so it can be rendered by the NavDisplay
    val backStack = mutableStateListOf(startKey)

    private fun updateBackStack() =
        backStack.apply {
            clear()
            addAll(topLevelStacks.flatMap { it.value })
        }

    private fun addTopLevel(key: T) {
        // If top level doesn't exist, add it
        if (topLevelStacks[key] == null) {
            topLevelStacks.put(key, mutableStateListOf(key))
        } else {
            topLevelStacks.apply {
                remove(key)?.let {
                    put(key, it)
                }
            }
        }
        topLevelKey = key
        updateBackStack()
    }

    private fun addNestedLevel(key: T) {
        topLevelStacks[topLevelKey]?.add(key)
        updateBackStack()
    }

    fun removeLast() {
        val removedKey = topLevelStacks[topLevelKey]?.removeLastOrNull()
        // If the removed key was a top level key, remove the associated top level stack
        topLevelStacks.remove(removedKey)
        topLevelKey = topLevelStacks.keys.last()
        updateBackStack()
    }

    fun addKey(key: T) {
        if (key is RootKey) {
            addTopLevel(key)
        } else {
            addNestedLevel(key)
        }
    }
}

class NavigationViewModel : ViewModel() {

    val topLevelBackStack: TopLevelBackStack<NavKey> = TopLevelBackStack(Logs)
}
