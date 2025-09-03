package com.robgasp.dailylog.core.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

@Composable
fun ErrorDialog(
    modifier: Modifier = Modifier,
    message: String = "Something went wrong! \uD83D\uDE22",
    title: String = "Error Message",
    actionLabel: String = "Ok",
    cancelable: Boolean = false,
    dismissLabel: String = "Dismiss",
    onDismiss: () -> Unit = {},
    onAction: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {
            if (cancelable) {
                onDismiss()
            }
        },
        modifier = modifier,
        properties = DialogProperties(
            dismissOnBackPress = cancelable,
            dismissOnClickOutside = cancelable,
        ),
        confirmButton = {
            TextButton(onClick = onAction) {
                Text(actionLabel)
            }
        },
        dismissButton = {
            if (cancelable) TextButton(onClick = onDismiss) { Text(dismissLabel) }
        },
        title = { Text(title) },
        text = { Text(message) }

    )
}
