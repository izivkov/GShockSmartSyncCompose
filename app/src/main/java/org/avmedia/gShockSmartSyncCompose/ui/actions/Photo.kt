import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gShockSmartSyncCompose.ui.common.AppIconFromResource

@Composable
fun Photo(
    modifier: Modifier = Modifier,
    cameraOrientation: String = "front",
    onOrientationChange: (String) -> Unit,
    actionEnabled: Boolean = true,
    onActionEnabledChange: (Boolean) -> Unit
) {
    AppCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon/ImageView equivalent
            AppIconFromResource(
                resourceId = R.drawable.camera,
            )

            // Text and Radio Buttons in a Row with weight
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title TextView equivalent
                Text(
                    text = stringResource(id = R.string.take_photo),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.weight(1f)) // Spacer equivalent to View with weight

                // Radio Buttons (Front/Back Camera)
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = cameraOrientation == "front",
                            onClick = { onOrientationChange("front") },
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(text = stringResource(id = R.string.front_cam))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = cameraOrientation == "back",
                            onClick = { onOrientationChange("back") },
                            modifier = Modifier.padding(4.dp)
                        )
                        Text(text = stringResource(id = R.string.back_cam))
                    }
                }
            }

            // SwitchMaterial equivalent
            AppSwitch(
                checked = actionEnabled,
                onCheckedChange = onActionEnabledChange,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPhoto() {
    Photo(modifier = Modifier, onActionEnabledChange = {}, onOrientationChange = {})
}
