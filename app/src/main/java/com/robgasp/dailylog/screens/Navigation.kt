package com.robgasp.dailylog.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.robgasp.dailylog.model.DLog
import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable object A: Screen
    @Serializable data class B(val id: String): Screen
    @Serializable data class C(val log: DLog): Screen
}

class NavigationViewModel: ViewModel() {
    val backStack: MutableList<Screen> = mutableStateListOf(Screen.A)
}