import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.actions.ActionsModel
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gShockSmartSyncCompose.ui.common.AppIconFromResource

@Composable
fun PhotoView(
    cameraOrientation: String = "front",
) {
    val title = stringResource(id = R.string.take_photo)
    val action = ActionsModel.actionMap[title] as ActionsModel.PhotoAction
    var isEnabled by remember { mutableStateOf(action.enabled) }
    var orientation by remember { mutableStateOf(action.cameraOrientation) }

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val context = LocalContext.current

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon/ImageView equivalent
            Box(modifier = Modifier.padding(4.dp)) {
                AppIconFromResource(
                    resourceId = R.drawable.camera,
                )
            }

            // Text and Radio Buttons in a Row with weight
            Row(
                modifier = Modifier
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title TextView equivalent
                AppTextLarge(
                    text = title,
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
                            selected = orientation == ActionsModel.CAMERA_ORIENTATION.FRONT,
                            onClick = {
                                action.cameraOrientation = ActionsModel.CAMERA_ORIENTATION.FRONT
                                action.save(context)
                                orientation = ActionsModel.CAMERA_ORIENTATION.FRONT
                            },
                            modifier = Modifier
                        )
                        Text(text = stringResource(id = R.string.front_cam))
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = orientation == ActionsModel.CAMERA_ORIENTATION.BACK,
                            onClick = {
                                action.cameraOrientation = ActionsModel.CAMERA_ORIENTATION.BACK
                                action.save(context)
                                orientation = ActionsModel.CAMERA_ORIENTATION.BACK
                            },
                            modifier = Modifier
                        )
                        Text(text = stringResource(id = R.string.back_cam))
                    }
                }
            }

            // SwitchMaterial equivalent
            AppSwitch(
                checked = isEnabled,
                onCheckedChange = { newValue ->
                    isEnabled = newValue // Update the state when the switch is toggled
                    action.enabled = newValue
                    action.save(context)
                },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPhoto() {
    PhotoView()
}
