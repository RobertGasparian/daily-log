package com.robgasp.dailylog.core.ui

import ShrinkWrap
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.robgasp.dailylog.R

@Composable
fun TopBar(
    screenTitle: String,
    hasBackAction: Boolean,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onAvatarClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
) {
    Column(
        modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(topBarHeight)
    ) {
        Row(Modifier.weight(1f)) {
            Icon(
                painter = painterResource(R.drawable.ic_avatar),
                contentDescription = "Account Icon",
                tint = Color.White,
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
                tint = Color.White,
                modifier = Modifier
                    .size(topBarIconSize)
                    .clickable {
                        onNotificationsClick()
                    },
            )
        }
        Spacer(Modifier.weight(0.3f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .animateContentSize()
        ) {
            ShrinkWrap(
                visible = hasBackAction,
                targetWidth = topBarIconSize + 8.dp, // spacer size
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(topBarIconSize)
                        .clickable {
                            onBackClick()
                        }
                )
                Spacer(Modifier.width(8.dp))
            }

            Text(
                text = screenTitle,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
    }
}
