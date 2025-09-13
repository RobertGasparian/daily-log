import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ShrinkWrap(visible: Boolean, targetWidth: Dp, content: @Composable () -> Unit) {
    val w by animateDpAsState(if (visible) targetWidth else 0.dp, label = "slotWidth")
    Box(Modifier.width(w), contentAlignment = Alignment.Center) {
        if (visible) content()
    }
}
