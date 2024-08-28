import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.avmedia.gShockSmartSyncCompose.R
import org.avmedia.gShockSmartSyncCompose.ui.common.AppCard
import org.avmedia.gShockSmartSyncCompose.ui.common.AppIconFromResource

@Composable
fun PhoneCall(
    modifier: Modifier = Modifier,
    onPhoneNumberChange: (String) -> Unit,
    isActionEnabled: Boolean,
    onActionEnabledChange: (Boolean) -> Unit
) {
    AppCard(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
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
                Text(
                    text = stringResource(id = R.string.make_phonecall),
                )
                TextField(
                    value = "", //stringResource(id = R.string._416_555_6789),
                    onValueChange = onPhoneNumberChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = TextStyle(fontSize = 20.sp),
                    placeholder = {
                        AppTextLarge(stringResource(id = R.string._416_555_6789)) // Placeholder text
                    }
                )
            }

            // Switch for Action Enable/Disable
            AppSwitch(
                checked = isActionEnabled,
                onCheckedChange = onActionEnabledChange,
                modifier = Modifier.padding(0.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPhoneCall() {
    PhoneCall(
        modifier = Modifier,
        onPhoneNumberChange = {},
        isActionEnabled = true,
        onActionEnabledChange = {})
}
