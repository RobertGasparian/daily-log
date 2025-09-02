package com.robgasp.dailylog.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopBar(
    screenTitle: String,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    Column(
        modifier
            .statusBarsPadding()
            .padding(16.dp)
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Row(Modifier.weight(1f)) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Account Icon",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onAvatarClick()
                    },
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = "Notifications Icon",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onNotificationsClick()
                    },
            )
        }
        Text(text = screenTitle, fontSize = 18.sp)
    }
}
