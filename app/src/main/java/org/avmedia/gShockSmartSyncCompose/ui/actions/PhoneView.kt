import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.actions.ActionsModel
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gShockSmartSyncCompose.ui.common.AppIconFromResource

@Composable
fun PhoneView() {
    val title = stringResource(id = R.string.make_phonecall)
    val action = ActionsModel.actionMap[title] as ActionsModel.PhoneDialAction
    var isEnabled by remember { mutableStateOf(action.enabled) }
    val context = LocalContext.current

    AppCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            AppIconFromResource(
                resourceId = R.drawable.phone,
                contentDescription = ""
            )

            // Title and EditText for Phone Number
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp)
            ) {
                AppTextLarge(
                    text = title,
                )
                Row {
                    var phoneNumber by remember { mutableStateOf(action.phoneNumber) }

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { newValue ->
                            phoneNumber = newValue
                            action.phoneNumber = newValue
                            action.save(context)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 80.dp),
                        textStyle = TextStyle(fontSize = 20.sp),
                        placeholder = {
                            AppTextLarge(stringResource(id = R.string._416_555_6789)) // Placeholder text
                        }
                    )
                }
            }

            // Switch for Action Enable/Disable
            AppSwitch(
                checked = isEnabled,
                onCheckedChange = { newValue ->
                    isEnabled = newValue // Update the state when the switch is toggled
                    action.enabled = newValue
                    action.save(context)
                },
                modifier = Modifier.padding(end = 0.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPhoneCall() {
    PhoneView()
}
