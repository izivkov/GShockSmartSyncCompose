package org.avmedia.gShockSmartSyncCompose.ui.actions

import AppSwitch
import AppText
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gShockSmartSyncCompose.ui.common.AppIconFromResource
import org.avmedia.gShockSmartSyncCompose.ui.common.HorizontalSeparator
import org.avmedia.gShockSmartSyncCompose.ui.common.InfoButton

typealias AppIconComposable = @Composable (imageVector: ImageVector) -> Unit

@Composable
fun ActionItem(
    modifier: Modifier = Modifier,
    title: String,
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    infoText: String? = null,
    resourceId: Int = R.drawable.generic_action_item
) {
    AppCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppIconFromResource(resourceId = resourceId)

                HorizontalSeparator(width = 4.dp)

                AppText(
                    text = title,
                    fontSize = 24.sp
                )

                infoText?.let {
                    HorizontalSeparator(width = 4.dp)
                    InfoButton(infoText = it)
                }
            }
            AppSwitch(
                checked = isEnabled,
                onCheckedChange = onEnabledChange
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActionItem() {
    ActionItem(
        title = "Action Item",
        isEnabled = true,
        onEnabledChange = {},
        infoText = null
    )
}
