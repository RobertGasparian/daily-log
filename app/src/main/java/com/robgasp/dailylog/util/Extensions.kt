package com.robgasp.dailylog.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

val DoNothing = Unit

suspend fun showDismissableSnackBar(
    snackbarHostState: SnackbarHostState,
    message: String,
    actionMessage: String? = null,
    onDismiss: () -> Unit = {}
) {
    val result = snackbarHostState.showSnackbar(
        message = message,
        duration = SnackbarDuration.Short,
        actionLabel = actionMessage ?: "Dismiss"
    )
    when (result) {
        SnackbarResult.Dismissed -> {
            onDismiss()
        }

        SnackbarResult.ActionPerformed -> {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}
